'use strict';

const React = require('react');
const client = require('./modules/client');
const stompClient = require('./modules/websocket-listener');

import Navbar from './components/Navbar'

export default class ChallengePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}, origChallenge:{}};
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: '/api/users/current',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            this.setState({user:response.entity});
        });

        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/challenges/' + id,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({origChallenge:response.entity});});
    }

    render () {
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        return (
            <div>
                <Navbar user={this.state.user} />
                <div className="container">
                    <div className="row">
                        <div className="col-sm-3 col-md-2 sidebar">
                            <ul className="nav nav-sidebar">
                                <li><a href="#">Summary (Not implemented) </a></li>
                                <li><a href="/user-habit">Habits</a></li>
                                <li><a href="#">Tasks (Not implemented)</a></li>
                                <li><a href="/challenge">Challenges</a></li>
                                <li><a href="#">Competition (Not implemented)</a></li>
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            <Challenge challenge={this.state.origChallenge}/>
                            <ChatDiv origChallenge={this.state.origChallenge}/>
                            <UserChallengesTable origchallenge={this.state.origChallenge} completed="false" title="User challenges"/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class UserChallengesTable extends React.Component {
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
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/challenges/' + id + '/userchallenges', /* /challenges/{challengeId}/userchallenges */
            params: par,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({challenges:response.entity._embedded.items});});
    }

    render () {
        var outs = this.state.challenges.map(it => <UserChallenge key={it._links.self.href} challenge={it}/>);
        return (
            <table className="table table-hover">
                <caption>{this.props.title}</caption>
                <thead>
                <tr>
                    <th>User name</th>
                    <th>Progress</th>
                    <th>Updated Date</th>
                    <th>Votes</th>
                    <th>Vote</th>
                </tr>
                </thead>
                <tbody>
                {outs}
                </tbody>
            </table>
        );
    }
}

class UserChallenge extends React.Component {
    constructor(props) {
        super(props);
        this.handleVote = this.handleVote.bind(this);
        this.challengeOnClick = this.challengeOnClick.bind(this);
    }

    handleVote() {
        /*client({
         method: 'PUT',
         path: habit.entity._links.self.href,
         headers: {
         'Content-Type': 'application/json',
         'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
         }
         }).done(response => {
         if (response.status.code === 204) {
         this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
         }
         /!* let the websocket handle updating the UI *!/},
         response => {
         if (response.status.code === 403) {
         alert('ACCESS DENIED: You are not authorized to delete ' +
         habit.entity._links.self.href);
         }
         });*/
    }

    challengeOnClick() {
        window.location = this.props.challenge._links.self.href;
    }

    render () {
        return (<tr>
                <td><a href='#'>User name</a></td>  {/*<a href={this.props.challenge._links.link.href}>{this.props.challenge.name}</a>*/}
                <td>
                    <div className="progress">
                        <div style={{width: '60%'}} aria-valuemax="100" aria-valuemin="0" aria-valuenow="60" role="progressbar" className="red progress-bar">
                            <span>60%</span>
                        </div>
                    </div>
                </td>
                <td>{(new Date(this.props.challenge.updatedWhen)).toUTCString()}</td>
                <td>{this.props.challenge.votes}</td>
                <td><button className="btn btn-lg btn-primary btn-block" onClick={this.handleVote}>Vote</button></td>
                </tr>
        );
    }
}

class Challenge extends React.Component {
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

class ChatDiv extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        if (this.props.origChallenge.acceptableForCurrentUser != undefined && !this.props.origChallenge.acceptableForCurrentUser) {
            return <h1>CHAT</h1>
        } else {
            return <h2>Chat is locked. Accept challenge for unlock.</h2>
        }
    }
}