'use strict';

const React = require('react');

export default class Habit extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        return (
            <div><h1 className="page-header">{this.props.habit.name}</h1>
                <h4>{this.props.habit.difficulty}</h4>
                <span className="text-muted">Difficulty</span>
                <h4>{this.props.habit.description}</h4>
                <span className="text-muted">Description</span>
                <h4>{this.props.habit.completedCount}</h4>
                <span className="text-muted">Completed (times)</span>
                <h4>{this.props.habit.achieved?'YES':'NO'}</h4>
                <span className="text-muted">Achieved</span>
            </div>
        );
    }
}