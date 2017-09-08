'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

import UpdateDialog from './UpdateDialog'

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
                    <UpdateDialog updatedEntity={this.props.habit}
                                  attributes={this.props.attributes.filter(attribute => attribute != 'createdWhen' && attribute != 'updatedWhen')}
                                  onUpdate={this.props.onUpdate} titleName="Update dsa" buttonName="Update"/>
                </td>
                <td>
                    <button className="btn btn-lg btn-primary btn-block" onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }
}

export default Habit;