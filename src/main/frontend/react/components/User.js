'use strict';

const React = require('react');

export default class User extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <h1 className="page-header">{this.props.user.firstName} {this.props.user.secondName} <a  className="text-muted" href="/edit">Edit</a></h1>
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
                        <div>
                            <h4>{this.props.user.username}</h4>
                            <span className="text-muted">User Name</span>
                            <h4>{this.props.user.firstName}</h4>
                            <span className="text-muted">First Name</span>
                            <h4>{this.props.user.secondName}</h4>
                            <span className="text-muted">Second Name</span>
                            <h4>{this.props.user.lastName}</h4>
                            <span className="text-muted">Last Name</span>
                            <h4>{this.props.user.email}</h4>
                            <span className="text-muted">Email</span>
                            <h4>{this.props.user.rank}</h4>
                            <span className="text-muted">Rank</span>
                            <h4>{this.props.user.score}</h4>
                            <span className="text-muted">All score</span>
                            <h4>{this.props.user.habitScore}</h4>
                            <span className="text-muted">Habit score</span>
                            <h4>{this.props.user.taskScore}</h4>
                            <span className="text-muted">Task score</span>
                            <h4> {(new Date(this.props.user.birthDate)).toUTCString()}</h4>
                            <span className="text-muted">Birth date</span>
                            <h4>{this.props.user.city}</h4>
                            <span className="text-muted">City</span>
                            <h4>{this.props.user.country}</h4>
                            <span className="text-muted">Country</span>
                            <h4>None</h4>
                            <span className="text-muted">Groups</span>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}