'use strict';

const React = require('react');
const client = require('./modules/client');

import Navbar from './components/Navbar'
import CreateChallenge from './components/CreateChallenge'
import AvailableChallengesTable from './components/AvailableChallengesTable'
import UserChallengesTable from './components/UserChallengesTable'

export default class ChallengesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}};
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
    }

    render () {
        return (
            <div>
                <Navbar user={this.state.user} />
                <div className="container">
                    <div className="row">
                        <div className="col-sm-3 col-md-2 sidebar">
                            <ul className="nav nav-sidebar">
                                <li><a href="/summary">Summary</a></li>
                                <li><a href="/user-habit">Habits</a></li>
                                <li><a href="#">Tasks (Not implemented)</a></li>
                                <li className="active"><a href="#">Challenges <span className="sr-only">(current)</span></a></li>
                                <li><a href="#">Competition (Not implemented)</a></li>
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            <CreateChallenge />
                            <AvailableChallengesTable created="false" title="Available challenges" divId="availableChallengesTableDiv1"/>
                            <div className="row nav-row">
                                <button data-toggle="collapse" data-target="#availableChallengesTableDiv2" type="button" className="btn btn-primary">Show/hide created by me challenges</button>
                            </div>
                            <AvailableChallengesTable created="true" title="Created by me" divId="availableChallengesTableDiv2" divClass="collapse"/>
                            <div className="row nav-row">
                                <button data-toggle="collapse" data-target="#userChallengesTableDiv" type="button" className="btn btn-primary">Show/hide accepted challenges</button>
                            </div>
                            <UserChallengesTable completed="false" title="Accepted challenges" divClass="collapse" divId="userChallengesTableDiv"/>
                            <div className="row nav-row">
                                <button data-toggle="collapse" data-target="#userChallengesTableDiv2" type="button" className="btn btn-primary">Show/hide completed challenges</button>
                            </div>
                            <UserChallengesTable completed="true" title="Accepted challenges" divClass="collapse" divId="userChallengesTableDiv2"/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}