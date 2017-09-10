'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

const when = require('when');

const client = require('./modules/client');
const follow = require('./modules/follow');
const stompClient = require('./modules/websocket-listener');
const root = '/api';
const GET_USER_HABITS_PATH = 'api/users/current/habits';

import HabitList from './components/habit-list-component'
import CreateDialog from './components/CreateDialog'

export default class UserHabitsPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {habits: [], attributes: [],  page: 1, pageSize: 10, links: {}, alert:null};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onComplete = this.onComplete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.refreshCurrentPage = this.refreshCurrentPage.bind(this);
        this.refreshAndGoToLastPage = this.refreshAndGoToLastPage.bind(this);
        this.showAlert = this.showAlert.bind(this);
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newHabit', callback: this.refreshAndGoToLastPage},// todo doesn't work normally
            {route: '/topic/updateHabit', callback: this.refreshCurrentPage}, //  doesn't work normally
            {route: '/topic/deleteHabit', callback: this.refreshCurrentPage} //  doesn't work normally
        ]);
    }

    loadFromServer(pageSize) {
        client({
            method: 'GET',
            path: GET_USER_HABITS_PATH,
            params: {size: pageSize},
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).then(habitCollection => {
            return client({
                method: 'GET',
                path: 'api/profile/habits',
                headers: {
                    'Accept': 'application/schema+json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).then(schema => {
                /**
                 * Filter unneeded JSON Schema properties, like uri references and
                 * subtypes ($ref).
                 */
                Object.keys(schema.entity.properties).forEach(function (property) {
                    if (schema.entity.properties[property].hasOwnProperty('format') &&
                        schema.entity.properties[property].format === 'uri') {
                        delete schema.entity.properties[property];
                    }
                    else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
                        delete schema.entity.properties[property];
                    }
                });

                this.schema = schema.entity;
                this.links = habitCollection.entity._links;
                return habitCollection;
            });
        }).then(habitCollection => {
            return habitCollection.entity._embedded.items.map(habit =>
                client({
                    method: 'GET',
                    path: habit._links.self.href,
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                    }
                })
            );
        }).then(habitPromises => {
            return when.all(habitPromises);
        }).done(habits => {
            this.setState({
                habits: habits,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: this.links
            });
        });
    }

    onCreate(newHabit) {
        follow(client, root, ['habits']).done(response => {
            client({
                method: 'POST',
                path: response.entity._links.self.href,
                entity: newHabit,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                if (response.status.code === 201) {
                    this.setState({alert:{entity:response.entity, message:'Created successfully! Created habit: '}})
                }
                /* Let the websocket handler create the state */
            }, response => {
                if (response.status.code === 500) {
                    alert('Required fields are not filled. Creation is failed.');
                }
            });
        })
    }

    onUpdate(habit, updatedHabit) {
        client({
            method: 'PUT',
            path: habit.entity._links.self.href,
            entity: updatedHabit,
            headers: {
                'Content-Type': 'application/json',
                'If-Match': habit.headers.Etag,
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            if (response.status.code === 200) {
                this.setState({alert:{entity:response.entity, message:'Updated habit: '}})
            }
            /* Let the websocket handler update the state */
        }, response => {
            if (response.status.code === 403) {
                alert('ACCESS DENIED: You are not authorized to update ' +
                    habit.entity._links.self.href);
            } else if (response.status.code === 412) {
                alert('DENIED: Unable to update ' +
                    habit.entity._links.self.href + '. Your copy is stale.');
            }
        });
    }

    onNavigate(navUri) {
        client({
            method: 'GET',
            path: navUri,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).then(habitCollection => {
            this.links = habitCollection.entity._links;

            return habitCollection.entity._embedded.items.map(habit =>
                client({
                    method: 'GET',
                    path: habit._links.self.href,
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                    }
                })
            );
        }).then(habitPromises => {
            return when.all(habitPromises);
        }).done(habits => {
            this.setState({
                habits: habits,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    onDelete(habit) {
        if(confirm('Delete the habit?')) {
            client({
                method: 'DELETE', path: habit.entity._links.self.href,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                if (response.status.code === 204) {
                    this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
                }
                /* let the websocket handle updating the UI */},
                response => {
                    if (response.status.code === 403) {
                        alert('ACCESS DENIED: You are not authorized to delete ' +
                            habit.entity._links.self.href);
                    }
                });
        }
    }

    onComplete(habit) {
        if(confirm('Complete the habit?')) {
            var href = habit.entity._links.self.href;
            var path = 'api/habits/' + href.substr(href.lastIndexOf('\\')) + '/complete';
            client({
                method: 'PUT',
                path: path,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                    if (response.status.code === 200) {
                        this.setState({alert:{entity:response.entity, message:'Completed habit:'}})
                    }
                    /* let the websocket handle updating the UI */},
                response => {
                    if (response.status.code === 403) {
                        alert('ACCESS DENIED: You are not authorized to complete ' +
                            habit.entity._links.self.href);
                    }
                });
        }
    }

    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    refreshAndGoToLastPage(message) {
        client({
            method: 'GET',
            path: GET_USER_HABITS_PATH,
            params: {size: this.state.pageSize},
        }).done(response => {
            if (response.entity._links.last !== undefined) {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        })
    }

    refreshCurrentPage(message) {
        client({
            method: 'GET',
            path: GET_USER_HABITS_PATH,
            params: {
                size: this.state.pageSize
            }
        }).then(habitCollection => {
            this.links = habitCollection.entity._links;
            this.page = habitCollection.entity.page;

            return habitCollection.entity._embedded.items.map(habit => {
                return client({
                    method: 'GET',
                    path: habit._links.self.href
                })
            });
        }).then(habitPromises => {
            return when.all(habitPromises);
        }).then(habits => {
            this.setState({
                page: this.page,
                habits: habits,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    showAlert() {
        if (this.state.alert != null) {
            return <EntityAlert message={this.state.alert.message} entity={this.state.alert.entity}/>;
        }
    }

    render() {
        var filteredAttrs = this.state.attributes.filter(attribute => attribute != 'createdWhen' && attribute != 'updatedWhen');
        return (
            <div>
                {this.showAlert()}
                <a href="#createHabit" data-toggle="modal" data-target="#createHabit">Create</a>
                <CreateDialog attributes={filteredAttrs} onCreate={this.onCreate} modalId="createHabit" titleName="Create new Habit" buttonName="Create"/>
                <HabitList habits={this.state.habits}
                           links={this.state.links}
                           pageSize={this.state.pageSize}
                           attributes={this.state.attributes}
                           onNavigate={this.onNavigate}
                           onUpdate={this.onUpdate}
                           onDelete={this.onDelete}
                           onComplete={this.onComplete}
                           updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}

class EntityAlert extends React.Component {
    constructor(props) {
        super(props);
    }

    showHref() {
        if (this.props.entity) {
            var href = this.props.entity._links.self.href;
            var path = 'habit/';
            var entityLink = path + href.substr(href.lastIndexOf('\\'));
            return <a href={entityLink}>{this.props.entity.name}</a>;
        }
    }

    render() {
        return (
            <div className="alert alert-success alert-dismissable">
                <a href="#" className="close" data-dismiss="alert" aria-label="close">&times;</a>
                {this.props.message} {this.showHref()}
            </div>
        );
    }
}