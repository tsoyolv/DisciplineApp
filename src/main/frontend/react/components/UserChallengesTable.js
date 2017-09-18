'use strict';

const React = require('react');
const client = require('./../modules/client');

const stompClient = require('./../modules/websocket-listener');

export default class UserChallengesTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {challenges: []};
        this.loadChallenges = this.loadChallenges.bind(this);
    }

    componentDidMount() {
        this.loadChallenges();
        stompClient.register([
            {route: '/topic/newUserChallenge', callback: this.loadChallenges},
            /*{route: '/topic/updateHabit', callback: this.refreshCurrentPage},
             {route: '/topic/deleteHabit', callback: this.refreshCurrentPage}*/
        ]);
    }

    loadChallenges() {
        var par = {};
        if (this.props.completed) {
            par = {completed: this.props.completed}
        }
        client({
            method: 'GET',
            path: '/api/users/current/userchallenges',
            params: par,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({challenges:response.entity._embedded.items});});
    }

    render () {
        var outs = this.state.challenges.map(it => <Challenge key={it._links.self.href} challenge={it}/>);
        return (
            <table className="table">
                <caption>{this.props.title}</caption>
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