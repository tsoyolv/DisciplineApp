'use strict';

const React = require('react');
const client = require('./../modules/client');

const stompClient = require('./../modules/websocket-listener');

export default class AvailableChallengesTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {challenges: []};
        this.loadFromServer = this.loadFromServer.bind(this);
    }

    componentDidMount() {
        stompClient.register([
            {route: '/topic/newChallenge', callback: this.loadFromServer},
            /*{route: '/topic/updateHabit', callback: this.refreshCurrentPage},
            {route: '/topic/deleteHabit', callback: this.refreshCurrentPage}*/
        ]);
    }

    loadFromServer() {
        if (this.props.user._links == null) {return;}
        client({
            method: 'GET',
            path: this.props.user._links.challenges.href,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(
            response => {
                this.setState({challenges:response.entity._embedded.items});
            });
    }

    render () {
        this.loadFromServer();
        var outs = this.state.challenges.map(it => <Challenge key={it._links.self.href} challenge={it}/>);
        return (
            <table className="table">
                <caption>Available challenges</caption>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Difficulty</th>
                    <th>Description</th>
                    <th>Challenge Date</th>
                    <th>Votes</th>
                    <th>Created by</th>
                    <th>Need in creator check</th>
                    <th>Vote</th>
                    <th>Accept</th>
                </tr>
                </thead>
                <tbody>
                {outs}
                </tbody>
            </table>
        );
    }
}

class Challenge extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        return (<tr>
                <td>{this.props.challenge.name}</td>
                <td>{this.props.challenge.difficulty}</td>
                <td>{this.props.challenge.description}</td>
                <td>{(new Date(this.props.challenge.challengeDate)).toUTCString()}</td>
                <td>{this.props.challenge.votes}</td>
                <td>{"created by"}</td>
                <td>{this.props.challenge.withCreator?'YES':'NO'}</td>
                <td>button vote</td>
                <td>button accept</td>
            </tr>
        );
    }
}