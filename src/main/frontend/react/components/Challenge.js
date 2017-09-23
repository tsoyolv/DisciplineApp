'use strict';

const React = require('react');

export default class Challenge extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        return (
            <div><h1 className="page-header">{this.props.challenge.name}</h1>
                <h4>{this.props.challenge.difficulty}</h4>
                <span className="text-muted">Difficulty</span>
                <h4>{this.props.challenge.description}</h4>
                <span className="text-muted">Description</span>
                <h4>{this.props.challenge.votes}</h4>
                <span className="text-muted">Votes</span>
            </div>
        );
    }
}