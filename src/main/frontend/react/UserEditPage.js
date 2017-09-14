'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./modules/client');
const when = require('when');
const stompClient = require('./modules/websocket-listener');

const GET_USER_PATH = '/api/users/current/edit';

import Navbar from './components/Navbar'

export default class UserEditPage extends React.Component {

    static httpGET(path) {
        return client({
            method: 'GET',
            path: path,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        })
    }

    constructor(props) {
        super(props);
        this.state = {user: {}, attributes: [], updated: false};
        this.onUpdate = this.onUpdate.bind(this);
        this.updateUserState = this.updateUserState.bind(this);
        this.showCompetition = this.showCompetition.bind(this);
        this.showChallenges = this.showChallenges.bind(this);
    }

    componentDidMount() {
        this.updateUserState();
        stompClient.register([
            {route: '/topic/updateUser', callback: this.updateUserState}
        ]);
    }

    updateUserState(stompMessage) {
        UserEditPage.httpGET(GET_USER_PATH).done(
            response => {
                var prop;
                var propArr = [];

                var responseEntity = response.entity;
                for (prop in responseEntity) {
                    propArr.push(prop);
                }
                var isUpdated = false;
                if (stompMessage) {isUpdated = true;}
                this.setState({user: responseEntity, attributes: propArr, updated:isUpdated});
            });
    }

    onUpdate(user, updatedUser) {
        client({
            method: 'PUT',
            path: user._links.self.href,
            entity: updatedUser,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            /* Let the websocket handler update the state */
        }, response => {
            if (response.status.code === 403) {
                alert('ACCESS DENIED: You are not authorized to update ' +
                    user._links.self.href);
            } else if (response.status.code === 412) {
                alert('DENIED: Unable to update ' +
                    user._links.self.href + '. Your copy is stale.');
            }
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
                        <div>
                            <h1 className="page-header">{this.state.user.firstName} {this.state.user.secondName}</h1>
                            <div className="row">
                                <UploadPhoto/>
                                <PersonalInfo user={this.state.user} attributes={this.state.attributes} onUpdate={this.onUpdate} updated={this.state.updated} />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        );
    }
}

class PersonalInfo extends React.Component {
    constructor(props) {
        super(props);
        this.onSave = this.onSave.bind(this);
    }

    static inputOnChange() {
        window.onbeforeunload = function () {
            return "You have unsaved changes on this page. Do you want to leave this page and discard your changes or stay on this page?";
        };
    }

    onSave(e) {
        e.preventDefault();
        var updatedUser = {};
        this.props.attributes.filter(attribute => attribute != 'hidden' && attribute != 'username').forEach(attribute => {
            updatedUser[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.user, updatedUser);
        window.location = "#";
        window.onbeforeunload = null;
    }

    showAlert() {
        if (this.props.updated) {
            return (
                <div className="alert alert-info alert-dismissable">
                    <i className="fa fa-coffee"/>
                    <strong>Changes saved.</strong> The new data will be displayed on your page.
                </div>
            );
        }
    }

    render() {
        var inputs = this.props.attributes.filter(attribute => attribute != 'hidden' && attribute != 'username').map(
            attribute =>
                <div key={attribute} className="form-group">
                    <label className="col-lg-3 control-label">{attribute}:</label>
                    <div className="col-lg-8">
                        <input className="form-control" type="text"
                               defaultValue={this.props.user[attribute]}
                               ref={attribute} onChange={PersonalInfo.inputOnChange}/>
                    </div>
                </div>
        );

        return(
            <div className="col-md-9 personal-info">
                {this.showAlert()}
                <form className="form-horizontal" role="form">
                    {inputs}

                    <div key="isHidden" className="form-group">
                        <label className="col-lg-3 control-label">Competitive:</label>
                        <div className="col-lg-8">
                            <div className="ui-select">
                                <select id="isHidden" className="form-control" ref="isHidden">
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div className="form-group">
                        <label className="col-md-3 control-label"></label>
                        <div className="col-md-8">
                            <input type="button" className="btn btn-primary" value="Save" onClick={this.onSave}/>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

class UploadPhoto extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="col-md-3">
                <div className="text-center">
                    <img src="//placehold.it/100" className="avatar img-circle" alt="avatar"/>
                    <h6>Upload a photo...</h6>

                    <input type="file" className="form-control"/>
                </div>
            </div>
        )
    }
}