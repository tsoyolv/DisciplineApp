'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

const client = require('./client');

const when = require('when');

const follow = require('./follow');

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

export default Habit;