'use strict';

const React = require('react');
const client = require('./../modules/client');

const stompClient = require('./../modules/websocket-listener');

export default class AvailableChallengesTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {challenges: []};
        this.loadChallenges = this.loadChallenges.bind(this);
    }

    componentDidMount() {
        this.loadChallenges();
        stompClient.register([
            {route: '/topic/newChallenge', callback: this.loadChallenges},
            {route: '/topic/updateChallenge', callback: this.loadChallenges}
            /*{route: '/topic/deleteHabit', callback: this.refreshCurrentPage}*/
        ]);
    }

    loadChallenges() {
        var par = {};
        if (this.props.created) {
            par = {createdBy: this.props.created}
        }
        client({
            method: 'GET',
            path: '/api/users/current/challenges',
            params: par,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({challenges:response.entity._embedded.items});});
    }

    render () {
        var outs = this.state.challenges.map(it => <Challenge key={it._links.self.href} challenge={it}/>);
        return (
            <table className="table table-hover">
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
        this.handleVote = this.handleVote.bind(this);
        this.handleAccept = this.handleAccept.bind(this);
    }

    handleVote() {
        client({
            method: 'PUT',
            path: this.props.challenge._links.vote.href,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
                if (response.status.code === 200) {
                   // this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
                }
                /* let the websocket handle updating the UI */},
            response => {
                if (response.status.code === 403) {
                    alert('ACCESS DENIED: You are not authorized to delete ' +
                        habit.entity._links.self.href);
                }
            });
    }

    handleAccept() {
        if(confirm('Accept the challenge?')) {
            client({
                method: 'PUT',
                path: this.props.challenge._links.accept.href,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                    if (response.status.code === 200) {
                        //this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
                    }
                    /* let the websocket handle updating the UI */},
                response => {
                    if (response.status.code === 403) {
                        alert('ACCESS DENIED: You are not authorized to delete ' +
                            habit.entity._links.self.href);
                    }
                });
        }
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
                <td><button className="btn btn-lg btn-primary btn-block" disabled={this.props.challenge.voteableForCurrentUser} onClick={this.handleVote}>Vote</button></td>
                <td><button className="btn btn-lg btn-primary btn-block" onClick={this.handleAccept}>Accept</button></td>
            </tr>
        );
    }
}