/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';

const React = require('react');
const ReactDOM = require('react-dom')
const client = require('./client');

const when = require('when');

const follow = require('./follow');

const stompClient = require('./websocket-listener');

const root = '/api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {habits: [], attributes: [],  page: 1, pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.refreshCurrentPage = this.refreshCurrentPage.bind(this);
        this.refreshAndGoToLastPage = this.refreshAndGoToLastPage.bind(this);
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newHabit', callback: this.refreshAndGoToLastPage},
            {route: '/topic/updateHabit', callback: this.refreshCurrentPage},
            {route: '/topic/deleteHabit', callback: this.refreshCurrentPage}
        ]);
    }

    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'habits', params: {size: pageSize}}]
        ).then(habitCollection => {
            return client({
                method: 'GET',
                path: habitCollection.entity._links.profile.href,
                headers: {
                    'Accept': 'application/schema+json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).then(schema => {
                this.schema = schema.entity;
                this.links = habitCollection.entity._links;
                return habitCollection;
            });
        }).then(habitCollection => {
            return habitCollection.entity._embedded.habits.map(habit =>
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
        var self = this;
        follow(client, root, ['habits']).then(response => {
            return client({
                method: 'POST',
                path: response.entity._links.self.href,
                entity: newHabit,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            })
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
            /* Let the websocket handler update the state */
        }, response => {
            if (response.status.code === 412) {
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

            return habitCollection.entity._embedded.habits.map(habit =>
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
        client({
            method: 'DELETE', path: habit.entity._links.self.href,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        });
    }

    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    refreshAndGoToLastPage(message) {
        follow(client, root, [{
            rel: 'habits',
            params: {size: this.state.pageSize}
        }]).done(response => {
            if (response.entity._links.last !== undefined) {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        })
    }

    refreshCurrentPage(message) {
        follow(client, root, [{
            rel: 'habits',
            params: {
                size: this.state.pageSize,
                page: this.state.page.number
            }
        }]).then(habitCollection => {
            this.links = habitCollection.entity._links;
            this.page = habitCollection.entity.page;

            return habitCollection.entity._embedded.habits.map(habit => {
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

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
                <HabitList habits={this.state.habits}
                           links={this.state.links}
                           pageSize={this.state.pageSize}
                           attributes={this.state.attributes}
                           onNavigate={this.onNavigate}
                           onUpdate={this.onUpdate}
                           onDelete={this.onDelete}
                           updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}

class HabitList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    render() {
        var habits = this.props.habits.map(habit =>
            <Habit key={habit.entity._links.self.href}
                   habit={habit}
                   attributes={this.props.attributes}
                   onUpdate={this.props.onUpdate}
                   onDelete={this.props.onDelete}/>
        );

        var navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                <input placeholder="page size" ref="pageSize" defaultValue={this.props.pageSize}
                       onInput={this.handleInput}/>
                <table>
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Difficulty</th>
                        <th>Description</th>
                        <th></th>
                        <th></th>
                    </tr>
                    {habits}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }

    handleInput(e) {
        e.preventDefault();
        var pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }
}

class Habit extends React.Component {
    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.habit);
    }

    render() {
        return (
            <tr>
                <td>{this.props.habit.entity.name}</td>
                <td>{this.props.habit.entity.difficulty}</td>
                <td>{this.props.habit.entity.description}</td>
                <td>
                    <UpdateDialog habit={this.props.habit}
                                  attributes={this.props.attributes}
                                  onUpdate={this.props.onUpdate}/>
                </td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }
}

class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var newHabit = {};
        this.props.attributes.forEach(attribute => {
            newHabit[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newHabit);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref={attribute} className="field"/>
            </p>
        );

        return (
            <div>
                <a href="#createHabit">Create</a>

                <div id="createHabit" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Create new habit</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var updatedHabit = {};
        this.props.attributes.forEach(attribute => {
            updatedHabit[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.habit, updatedHabit);
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.map(attribute =>
            <p key={this.props.habit.entity[attribute]}>
                <input type="text" placeholder={attribute}
                       defaultValue={this.props.habit.entity[attribute]}
                       ref={attribute} className="field"/>
            </p>
        );

        var dialogId = "updateHabit-" + this.props.habit.entity._links.self.href;

        return (
            <div key={this.props.habit.entity._links.self.href}>
                <a href={"#" + dialogId}>Update</a>
                <div id={dialogId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Update an habit</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Update</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

};

ReactDOM.render(
    <App />,
    document.getElementById('react')
)