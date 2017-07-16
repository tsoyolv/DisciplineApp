/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const when = require('when');

const follow = require('./follow');

const stompClient = require('./websocket-listener');

const root = '/api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {habits: [], attributes: [],  page: 1, pageSize: 10, links: {}};
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
        if(confirm('Delete the habit?')) {
            client({
                method: 'DELETE', path: habit.entity._links.self.href,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {/* let the websocket handle updating the UI */},
                response => {
                    if (response.status.code === 403) {
                        alert('ACCESS DENIED: You are not authorized to delete ' +
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
                <table className="table table-striped">
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Difficulty</th>
                        <th>Description</th>
                        <th>Created when</th>
                        <th>Updated when</th>
                        <th>User</th>
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
                <td>{(new Date(this.props.habit.entity.createdWhen)).toUTCString()}</td>
                <td>{(new Date(this.props.habit.entity.updatedWhen)).toUTCString()}</td>
                <td>{this.props.habit.entity.habitUser.username}</td>
                <td>
                    <UpdateDialog habit={this.props.habit}
                                  attributes={this.props.attributes}
                                  onUpdate={this.props.onUpdate}/>
                </td>
                <td>
                    <button className="btn btn-lg btn-primary btn-block" onClick={this.handleDelete}>Delete</button>
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
        var created_attributes = this.props.attributes.filter(attribute => attribute != 'createdWhen' && attribute != 'updatedWhen');
        created_attributes.forEach(attribute => {
            newHabit[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newHabit);

        // clear out the dialog's inputs
        created_attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.filter(attribute => attribute != 'createdWhen' && attribute != 'updatedWhen')
        .map(attribute =>
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
        this.update_attributes = props.attributes.filter(attribute => attribute != 'createdWhen' && attribute != 'updatedWhen');
    }

    handleSubmit(e) {
        e.preventDefault();
        var updatedHabit = {};
        this.update_attributes.forEach(attribute => {
            updatedHabit[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.habit, updatedHabit);
        window.location = "#";
    }

    render() {
        var inputs = this.update_attributes.map(attribute =>
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

}
/*ReactDOM.render(
    <App />,
    document.getElementById('all_habits')
);*/

/*-===================================================== EXAMPLE START ==============================================-*/
class Clock extends React.Component {

    /* component constructor. props are immutable, state can be changed */
    constructor(props) {
        super(props);
        this.state = {date: new Date()};
        console.log('Clock is initialized');
    }

    /* component function. The componentDidMount() hook runs after the component output has been rendered to the DOM */
    componentDidMount() {
        this.timerID = setInterval(
            () => this.tick(),
            1000
        );
    }

    /* component function. If the Clock component is ever removed from the DOM, React calls the componentWillUnmount() */
    componentWillUnmount() {
        clearInterval(this.timerID);
    }

    /* Thanks to the setState() call, React knows the state has changed, and calls render() method again to learn
    what should be on the screen. */
    /*  state is often called local or encapsulated. It is not accessible to any component other than the one that owns
        and sets it. */
    tick() {
        this.setState({
            date: new Date()
        });
    }

    /* component function */
    render() {
        return (
            <div>
                <h2>It is {this.state.date.toLocaleTimeString()}.</h2>
            </div>
        );
    }
}

class Toggle extends React.Component {
    constructor(props) {
        super(props);
        this.state = {isToggleOn: true};

        /* This binding is necessary to make `this` work in the callback */
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.setState(prevState => ({
            isToggleOn: !prevState.isToggleOn
        }));
    }

    /* In rare cases you might want a component to hide itself even though it was rendered by another component.
       To do this return null instead of its render output. */
    render() {
        return (
            <div>
            <button onClick={this.handleClick}>
                {this.state.isToggleOn ? 'Show clock' : 'Hide clock'}
            </button>
            {this.state.isToggleOn ? null : <Clock/>}
            </div>
        );
    }
}

function ListItem(props) {
    // Correct! There is no need to specify the key here:
    return <li>{props.value}</li>;
}

function NumberList(props) {
    const numbers = props.numbers;
    const listItems = numbers.map((number) =>
        // Correct! Key should be specified inside the array.
        <ListItem key={number.toString()}
                  value={number} />
        /* Keys serve as a hint to React but they don't get passed to your components.
           If you need the same value in your component, pass it explicitly as a prop with a different name */
        /* Keys used within arrays should be unique among their siblings. However they don't need to be globally unique.
           We can use the same keys when we produce two different arrays */
    );

    /* simple list example */
    /*const listItems = numbers.map((numbers) =>
        <li>{numbers}</li>
    );*/

    return (
        <ul>
            {listItems}
        </ul>
    );
}

/* JSX allows embedding any expressions in curly braces so we could inline the map() result: */
/*function NumberList(props) {
    const numbers = props.numbers;
    return (
        <ul>
            {numbers.map((number) =>
                <ListItem key={number.toString()}
                          value={number} />
            )}
        </ul>
    );
}*/

class NameForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    /* you cannot return false to prevent default behavior in React. You must call preventDefault explicitly. */
    handleSubmit(event) {
        alert('A name was submitted: ' + this.state.value);
        event.preventDefault();
    }
    /* For example, with plain HTML, to prevent the default link behavior of opening a new page, you can write:
     <a href="#" onclick="console.log('The link was clicked.'); return false">
     Click me
     </a> */

    /* In React, a <textarea> uses a 'value' attribute. (not tag's child like in HTML)*/
    /* Overall, this makes it so that <input type="text">, <textarea>, and <select> all work very similarly -
       they all accept a value attribute that you can use to implement a controlled component. */

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.value} onChange={this.handleChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}

class FlavorForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: 'coconut'};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        alert('Your favorite flavor is: ' + this.state.value);
        event.preventDefault();
    }

    /* React, instead of using this selected attribute, uses a value attribute on the root select tag.
       This is more convenient in a controlled component because you only need to update it in one place. */
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Pick your favorite La Croix flavor:
                    <select value={this.state.value} onChange={this.handleChange}>
                        <option value="grapefruit">Grapefruit</option>
                        <option value="lime">Lime</option>
                        <option value="coconut">Coconut</option>
                        <option value="mango">Mango</option>
                    </select>
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}

class Reservation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isGoing: true,
            numberOfGuests: 2
        };

        this.handleInputChange = this.handleInputChange.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }
    /* [name]: value - It is equivalent to this ES5 code:
     var partialState = {};
     partialState[name] = value;
     this.setState(partialState); */

    render() {
        return (
            <form>
                <label>
                    Is going:
                    <input
                        name="isGoing"
                        type="checkbox"
                        checked={this.state.isGoing}
                        onChange={this.handleInputChange} />
                </label>
                <br />
                <label>
                    Number of guests:
                    <input
                        name="numberOfGuests"
                        type="number"
                        value={this.state.numberOfGuests}
                        onChange={this.handleInputChange} />
                </label>
            </form>
        );
    }
}

/* Lifting state up start */
const scaleNames = {
    c: 'Celsius',
    f: 'Fahrenheit'
};

function toCelsius(fahrenheit) {
    return (fahrenheit - 32) * 5 / 9;
}

function toFahrenheit(celsius) {
    return (celsius * 9 / 5) + 32;
}

function tryConvert(temperature, convert) {
    const input = parseFloat(temperature);
    if (Number.isNaN(input)) {
        return '';
    }
    const output = convert(input);
    const rounded = Math.round(output * 1000) / 1000;
    return rounded.toString();
}

function BoilingVerdict(props) {
    if (props.celsius >= 100) {
        return <p>The water would boil.</p>;
    }
    return <p>The water would not boil.</p>;
}

class TemperatureInput extends React.Component {
    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    /* React calls the function specified as onChange on the DOM <input> */
    /* Its props, including onTemperatureChange, were provided by its parent component, the Calculator */
    /* lift this state up to Calculator  <input> onChange */
    handleChange(e) {
        this.props.onTemperatureChange(e.target.value);
    }

    render() {
        const temperature = this.props.temperature;
        const scale = this.props.scale;
        return (
            <fieldset>
                <legend>Enter temperature in {scaleNames[scale]}:</legend>
                <input value={temperature}
                       onChange={this.handleChange} />
            </fieldset>
        );
    }
}

class Calculator extends React.Component {
    constructor(props) {
        super(props);
        this.handleCelsiusChange = this.handleCelsiusChange.bind(this);
        this.handleFahrenheitChange = this.handleFahrenheitChange.bind(this);
        this.state = {temperature: '', scale: 'c'};
    }

    /* the Calculator component asks React to re-render itself by calling this.setState() */
    handleCelsiusChange(temperature) {
        this.setState({scale: 'c', temperature});
    }

    handleFahrenheitChange(temperature) {
        this.setState({scale: 'f', temperature});
    }

    /* React calls the Calculator component's render method to learn what the UI should look like. The values of both
       inputs are recomputed based on the current temperature and the active scale. The temperature conversion is
       performed here. */
    /* React calls the render methods of the individual TemperatureInput components with their new props specified by
       the Calculator. It learns what their UI should look like. */
    render() {
        const scale = this.state.scale;
        const temperature = this.state.temperature;
        const celsius = scale === 'f' ? tryConvert(temperature, toCelsius) : temperature;
        const fahrenheit = scale === 'c' ? tryConvert(temperature, toFahrenheit) : temperature;

        return (
            <div>
                <TemperatureInput
                    scale="c"
                    temperature={celsius}
                    onTemperatureChange={this.handleCelsiusChange} />
                <TemperatureInput
                    scale="f"
                    temperature={fahrenheit}
                    onTemperatureChange={this.handleFahrenheitChange} />
                <BoilingVerdict
                    celsius={parseFloat(celsius)} />
            </div>
        );
    }
}
/* Lifting stat up end */

/* Composition start */

function FancyBorder(props) {
    /* without props.children it doesn't work */
    return (
        <div className={'FancyBorder FancyBorder-' + props.color}>
            {props.children}
        </div>
    );
}

function WelcomeDialog() {
    return (
        <FancyBorder color="blue">
            <h3 className="Dialog-title">
                Welcome
            </h3>
            <p className="Dialog-message">
               This is composition with props.children.
            </p>
        </FancyBorder>
    );
}


function Contacts() {
    return <p>contacts</p>;
}

function Chat() {
    return <p>chat</p>;
}

function SplitPane(props) {
    return (
        <div className="SplitPane">
            <div className="SplitPane-left">
                {props.left}
            </div>
            <div className="SplitPane-right">
                {props.right}
            </div>
        </div>
    );
}

function CompositionWithCustomChilds() {
    return (
        <SplitPane
            left={
                <Contacts />
            }
            right={
                <Chat />
            } />
    );
}

/* Composition end */

const numbers = [1, 2, 3];

class Example extends React.Component {
    render() {
        return (
            <div>
                <h2>State</h2>
                <Toggle />
                <h2>List</h2>
                <NumberList numbers={numbers} />
                <h2>Event handling</h2>
                <NameForm/>
                <h2>Select</h2>
                <FlavorForm/>
                <h2>Multiple inputs</h2>
                <Reservation/>
                <h2>Lifting state up</h2>
                <Calculator/>
                <h2>Composition</h2>
                <WelcomeDialog/>
                <h2>Composition with custom childs</h2>
                <CompositionWithCustomChilds/>
                <h2>Habits table</h2>
                <App />
            </div>
        )
    }
}

ReactDOM.render(
    <Example />,
    document.getElementById('all_habits')
);
/*-===================================================== EXAMPLE END ================================================-*/