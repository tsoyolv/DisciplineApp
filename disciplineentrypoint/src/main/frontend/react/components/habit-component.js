'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

import UpdateDialog from './UpdateDialog'

class Habit extends React.Component {
    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleComplete = this.handleComplete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.habit);
    }

    handleComplete() {
        this.props.onComplete(this.props.habit);
    }

    render() {
        var href = this.props.habit.entity._links.self.href;
        var habitHref = 'habit/' + href.substr(href.lastIndexOf('\\'));
        var filteredAttrs = this.filterUpdateAttrs();
        return (
            <tr>
                <td><a href={habitHref}>{this.props.habit.entity.name}</a></td>
                <td>{this.props.habit.entity.difficulty}</td>
                <td>{this.props.habit.entity.description}</td>
                <td>{(new Date(this.props.habit.entity.createdWhen)).toUTCString()}</td>
                <td>{(new Date(this.props.habit.entity.updatedWhen)).toUTCString()}</td>
                <td><button className="btn btn-lg btn-primary btn-block" onClick={this.handleComplete}>Complete</button></td>
                <td>
                    <UpdateDialog updatedEntity={this.props.habit}
                                  attributes={filteredAttrs}
                                  onUpdate={this.props.onUpdate} titleName="Update dsa" buttonName="Update"/>
                </td>
                <td>
                    <button className="btn btn-lg btn-primary btn-block" onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }

    filterUpdateAttrs() {
        return this.props.attributes.filter(attribute =>
        attribute != 'createdWhen' &&
        attribute != 'updatedWhen' &&
        attribute != 'nonCompletedCount' &&
        attribute != 'achieved' &&
        attribute != 'completed' &&
        attribute != 'completedCount');
    }
}

export default Habit;