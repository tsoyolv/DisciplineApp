'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./modules/client');
const when = require('when');
const stompClient = require('./modules/websocket-listener');

const GET_USER_PATH = '/api/users/current/edit';

export default class UserEditApp extends React.Component {

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
        this.state = {user: {}, attributes: []};
        this.onUpdate = this.onUpdate.bind(this);
        this.updateUserState = this.updateUserState.bind(this);
    }

    componentDidMount() {
        this.updateUserState();
        stompClient.register([
            {route: '/topic/updateUser', callback: this.updateUserState}
        ]);
    }

    updateUserState() {
        UserEditApp.httpGET(GET_USER_PATH).done(
            response => {
                var prop;
                var propArr = [];

                var responseEntity = response.entity;
                for (prop in responseEntity) {
                    propArr.push(prop);
                }
                this.setState({user: responseEntity, attributes: propArr});
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

    render() {
        return (
            <div>
                <h1 className="page-header">{this.state.user.firstName} {this.state.user.secondName}</h1>
                <div className="row">
                    <UploadPhoto/>
                    <PersonalInfo user={this.state.user} attributes={this.state.attributes} onUpdate={this.onUpdate} />
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

    onSave(e) {
        e.preventDefault();
        var updatedUser = {};
        this.props.attributes.forEach(attribute => {
            updatedUser[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.user, updatedUser);
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.filter(attribute => attribute != 'isHidden').map(
            attribute =>
                <div key={attribute} className="form-group">
                    <label className="col-lg-3 control-label">{attribute}:</label>
                    <div className="col-lg-8">
                        <input className="form-control" type="text"
                               defaultValue={this.props.user[attribute]}
                               ref={attribute}/>
                    </div>
                </div>
        );

        return(
            <div className="col-md-9 personal-info">
                <Alert/>
                <h3>Personal info</h3>
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
                            <input type="button" className="btn btn-primary" value="Save Changes" onClick={this.onSave}/>
                            <span></span>
                            <input type="reset" className="btn btn-default" value="Cancel"/>
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

class Alert extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <div className="alert alert-info alert-dismissable">
            <a className="panel-close close" data-dismiss="alert">×</a>
            <i className="fa fa-coffee"/>
            This is an <strong>.alert</strong>. Use this to show important messages to the user.
        </div>;
    }
}