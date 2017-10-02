'use strict';

const React = require('react');

export default class User extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <h1 className="page-header">{this.props.user.firstName} {this.props.user.secondName}
                    <span className="pull-right">
                        <a href="/edit"><button className="btn btn-sm btn-warning" type="button"
                                data-toggle="tooltip"
                                data-original-title="Edit this user">
                            <i className="glyphicon glyphicon-edit"></i>
                        </button></a>
                    </span>
                </h1>

                Progress <span className="text-muted">(Lvl {this.props.user.level})</span>
                <div className="progress">
                    <div className="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style={{width: this.props.user.levelPercentage + '%'}}>{this.props.user.levelPercentage + '%'}</div>
                </div>
                <div className="row">
                    <div className="col-md-3">
                        <img src="images/image.jpg" width="200" height="200" className="img-responsive"
                             alt="Generic placeholder thumbnail"/>
                        Satiety
                        <div className="progress">
                            <div className="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style={{width: this.props.user.progressPerDay + '%'}}>{this.props.user.progressPerDay + '%'}</div>
                        </div>
                    </div>
                    <div className="col-md-9 personal-info">
                        <div className="panel-body">
                            <div className="row">
                                <div className=" col-md-9 col-lg-9 hidden-xs hidden-sm">
                                    <strong>Info</strong><br></br>
                                    <table className="table table-user-information">
                                        <tbody>
                                        <tr>
                                            <td>User Name:</td>
                                            <td><strong>{this.props.user.username}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>First Name:</td>
                                            <td><strong>{this.props.user.firstName}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Second Name:</td>
                                            <td><strong>{this.props.user.secondName}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Last Name:</td>
                                            <td><strong>{this.props.user.lastName}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Email:</td>
                                            <td><strong>{this.props.user.email}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Rank:</td>
                                            <td><strong>{this.props.user.rank}</strong></td>
                                        </tr>
                                        <tr>
                                            <td>All score:</td>
                                            <td><strong>{this.props.user.score}</strong></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <button className="btn btn-primary" data-toggle="collapse" data-target="#demo">Additional info</button>
                                    <div id="demo" className="collapse">
                                        <table className="table table-user-information">
                                            <tbody>
                                            <tr>
                                                <td>Habit score:</td>
                                                <td><strong>{this.props.user.habitScore}</strong></td>
                                            </tr>
                                            <tr>
                                                <td>Task score:</td>
                                                <td><strong>{this.props.user.taskScore}</strong></td>
                                            </tr>
                                            <tr>
                                                <td>Birthday:</td>
                                                <td><strong>{(new Date(this.props.user.birthDate)).toUTCString()}</strong></td>
                                            </tr>
                                            <tr>
                                                <td>City:</td>
                                                <td><strong>{this.props.user.city}</strong></td>
                                            </tr>
                                            <tr>
                                                <td>Country:</td>
                                                <td><strong>{this.props.user.country}</strong></td>
                                            </tr>
                                            <tr>
                                                <td>Groups:</td>
                                                <td><strong>not implemented</strong></td>
                                            </tr></tbody></table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}