/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';

const React = require('react');
const ReactDOM = require('react-dom')
const client = require('./client');

const follow = require('./follow');

const root = '/api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {habits: [], attributes: [], pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'habits', params: {size: pageSize}}]
        ).then(habitCollection => {
            return client({
                method: 'GET',
                path: habitCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return habitCollection;
            });
        }).done(habitCollection => {
            this.setState({
                habits: habitCollection.entity._embedded.habits,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: habitCollection.entity._links});
        });
    }

    onCreate(newHabit) {
        follow(client, root, ['habits']).then(habitCollection => {
            return client({
                method: 'POST',
                path: habitCollection.entity._links.self.href,
                entity: newHabit,
                headers: {'Content-Type': 'application/json',
                            'X-CSRF-TOKEN' : $("meta[name='_csrf']").attr("content")}
            })
        }).then(response => {
            return follow(client, root, [
                {rel: 'habits', params: {'size': this.state.pageSize}}]);
        }).done(response => {
            if (typeof response.entity._links.last != "undefined") {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        });
    }

    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(habitCollection => {
            this.setState({
                habits: habitCollection.entity._embedded.habits,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: habitCollection.entity._links
            });
        });
    }

    onDelete(habit) {
        client({method: 'DELETE', path: habit._links.self.href,
            headers: {'Content-Type': 'application/json',
            'X-CSRF-TOKEN' : $("meta[name='_csrf']").attr("content")} }).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
                <HabitList habits={this.state.habits}
                           links={this.state.links}
                           pageSize={this.state.pageSize}
                           onNavigate={this.onNavigate}
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
            <Habit key={habit._links.self.href} habit={habit} onDelete={this.props.onDelete}/>
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
                <input placeholder="page size" ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
                <table>
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Difficulty</th>
                        <th>Description</th>
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

    handleNavFirst(e){
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
                <td>{this.props.habit.name}</td>
                <td>{this.props.habit.difficulty}</td>
                <td>{this.props.habit.description}</td>
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
                <input type="text" placeholder={attribute} ref={attribute} className="field" />
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

ReactDOM.render(
    <App />,
    document.getElementById('react')
)