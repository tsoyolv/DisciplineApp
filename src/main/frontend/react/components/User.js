'use strict';

const React = require('react');

export default class User extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <h1 className="page-header">{this.props.user.firstName} {this.props.user.secondName}</h1>
                <div className="row placeholders">
                    <div className="col-xs-6 col-sm-3 placeholder">
                        <img src="images/image.jpg" width="200" height="200" className="img-responsive"
                             alt="Generic placeholder thumbnail"/>
                    </div>
                    <div className="col-xs-6 col-sm-3 placeholder">
                        <div>
                            <span className="text-muted">User Name</span>
                            <h4>{this.props.user.username}</h4>
                            <span className="text-muted">First Name</span>
                            <h4>{this.props.user.firstName}</h4>
                            <span className="text-muted">Second Name</span>
                            <h4>{this.props.user.secondName}</h4>
                            <span className="text-muted">Last Name</span>
                            <h4>{this.props.user.lastName}</h4>
                            <span className="text-muted">Email</span>
                            <h4>{this.props.user.email}</h4>
                            <span className="text-muted">All score</span>
                            <h4>{this.props.user.score}</h4>
                            <span className="text-muted">Habit score</span>
                            <h4>{this.props.user.habitScore}</h4>
                            <span className="text-muted">Task score</span>
                            <h4>{this.props.user.taskScore}</h4>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}