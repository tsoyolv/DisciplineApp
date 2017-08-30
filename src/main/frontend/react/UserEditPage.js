'use strict';

const React = require('react');
const client = require('./modules/client');
const when = require('when');

const GET_USER_PROFILE_PATH = '/api/profile/users';

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
    }

    componentDidMount() {
        UserEditApp.httpGET(GET_USER_PROFILE_PATH).done(response => { this.setState({attributes:response.entity}); });
        var user = this.state.attributes;
        user.toString();

        UserEditApp.httpGET(GET_USER_PATH).done(response => { this.setState({user:response.entity}); });
        /*.then(schema => {
                /!**
                 * Filter unneeded JSON Schema properties, like uri references and
                 * subtypes ($ref).
                 *!/
                Object.keys(schema.entity.properties).forEach(function (property) {
                    if (schema.entity.properties[property].hasOwnProperty('format') &&
                        schema.entity.properties[property].format === 'uri') {
                        delete schema.entity.properties[property];
                    }
                    else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
                        delete schema.entity.properties[property];
                    }
                });

                this.schema = schema.entity;
                this.links = habitCollection.entity._links;
                return habitCollection;
            });*/
    }

    onUpdate(user, updatedUser) {
        client({
            method: 'PUT',
            path: user._links.self.href,
            entity: updatedUser,
            headers: {
                'Content-Type': 'application/json',
                'If-Match': user.headers.Etag,
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
        return <UserUpdate user={this.state.user} attributes={this.state.attributes} onUpdate={this.onUpdate}  />;
    }
}

class UserUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.update_attributes = props.attributes;
    }

    handleSubmit(e) {
        e.preventDefault();
        var updatedUser = {};
        this.update_attributes.forEach(attribute => {
            updatedUser[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.user, updatedUser);
        window.location = "#";
    }

    render() {
        var inputs = this.update_attributes.map(attribute =>
            <div key={this.props.user[attribute]} className="form-group">
                <label className="col-lg-3 control-label">{attribute}:</label>
                <div className="col-lg-8">
                    <input className="form-control" type="text"
                           defaultValue={this.props.user[attribute]}
                           ref={attribute}/>
                </div>
            </div>
        );


        return <div>
            <h1 className="page-header">Oleg Tsoi</h1>
            <div className="row">
                {/*left column*/}
                <div className="col-md-3">
                    <div className="text-center">
                        <img src="//placehold.it/100" className="avatar img-circle" alt="avatar"/>
                        <h6>Upload a photo...</h6>

                        <input type="file" className="form-control"/>
                    </div>
                </div>

                {/* edit form column*/}
                <div className="col-md-9 personal-info">
                    <div className="alert alert-info alert-dismissable">
                        <a className="panel-close close" data-dismiss="alert">×</a>
                        <i className="fa fa-coffee"></i>
                        This is an <strong>.alert</strong>. Use this to show important messages to the user.
                    </div>
                    <h3>Personal info</h3>
                    <form className="form-horizontal" role="form">
                        {inputs}
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Drop down example:</label>
                            <div className="col-lg-8">
                                <div className="ui-select">
                                    <select id="user_time_zone" className="form-control">
                                        <option value="Hawaii">(GMT-10:00) Hawaii</option>
                                        <option value="Pacific Time (US &amp; Canada)">(GMT-08:00) Pacific Time (US &amp; Canada)</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-md-3 control-label"></label>
                            <div className="col-md-8">
                                <input type="button" className="btn btn-primary" value="Save Changes" onClick={this.handleSubmit}/>
                                <span></span>
                                <input type="reset" className="btn btn-default" value="Cancel"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>;
    }
}