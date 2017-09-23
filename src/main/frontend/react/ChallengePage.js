'use strict';

const React = require('react');
const client = require('./modules/client');
const stompClient = require('./modules/websocket-listener');

import Navbar from './components/Navbar'
import Challenge from './components/Challenge'
import UserChallengeTableRow from './components/UserChallengeTableRow'
import ChatDiv from './components/ChatDiv'

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
        var id = href.substr(href.lastIndexOf('/') + 1);
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
        var id = href.substr(href.lastIndexOf('/') + 1);
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
                            <ChatDiv origChallenge={this.state.origChallenge} user={this.state.user}/>
                            <UserChallengesTable origchallenge={this.state.origChallenge} completed="false" title="Accepted challenges"/>
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
        var id = href.substr(href.lastIndexOf('/') + 1);
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
        var outs = this.state.challenges.map(it => <UserChallengeTableRow key={it._links.self.href} challenge={it}/>);
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