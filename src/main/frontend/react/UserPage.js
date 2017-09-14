'use strict';

const React = require('react');
const client = require('./modules/client');
const when = require('when');

const GET_USER_PATH = '/api/users/current';

import User from './components/User'
import Navbar from './components/Navbar'

export default class UserPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {user: {}};
        this.showCompetition = this.showCompetition.bind(this);
        this.showChallenges = this.showChallenges.bind(this);
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: GET_USER_PATH,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            this.setState({user:response.entity});
        });
    }

    showCompetition() {
        if (this.state.user) {
            if (this.state.user.hidden != null && !this.state.user.hidden) {
                return (<li><a href="#">Competition (Not implemented)</a></li>);
            }
        }
    }

    showChallenges() {
        if (this.state.user) {
            if (this.state.user.hidden != null && !this.state.user.hidden) {
                return (<li><a href="/challenge">Challenges</a></li>);
            }
        }
    }

    render() {
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
                                    {this.showChallenges()}
                                    {this.showCompetition()}
                                </ul>
                            </div>
                            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                                <User user={this.state.user}/>
                            </div>
                        </div>
                    </div>
                </div>
            );
    }
}