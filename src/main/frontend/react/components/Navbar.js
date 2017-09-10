'use strict';

const React = require('react');
const client = require('./modules/client');

export default class Navbar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: props.user, notifications:props.notifications};
        this.signOff = this.signOff.bind(this);
    }

    componentDidMount() {
        if (!this.state.user) {
            client({
                method: 'GET',
                path: '/api/users/current',
                headers: {
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                this.setState({user: response.entity});
            });
        }
    }

    signOff(e) {
        client({
            method: 'POST',
            path: '/logout',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        })
    }

    render() {
        return (
            <nav className="navbar navbar-default navbar-fixed-top">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a className="navbar-brand" href="#">Discipline</a>
                    </div>
                    <div id="navbar" className="navbar-collapse collapse">
                        <ul className="nav navbar-nav">
                            <li className="dropdown">
                                <a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                                   aria-expanded="false"><i className="fa fa-bell-o fa-lg" aria-hidden="true"></i><span className="caret"></span></a>
                                <ul className="dropdown-menu">
                                    <li><a>Notifications are not implemented</a></li>
                                </ul>
                            </li>
                        </ul>
                        <ul className="nav navbar-nav navbar-right">
                            <li className="dropdown">
                                <a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                                   aria-expanded="false">{this.state.user.username} <span className="caret"></span></a>
                                <ul className="dropdown-menu">
                                    <li><a href="#" onClick="alert('Not implemented')" className="navbar-link disabled">Settings</a></li>
                                    <li role="separator" className="divider"></li>
                                    <li><a href="#" onClick={this.signOff}>Sign off</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </nav>
        );
    }
}