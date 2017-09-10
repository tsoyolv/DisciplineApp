'use strict';

const React = require('react');
const client = require('./../modules/client');

export default class Navbar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: props.user, notifications:props.notifications};
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

    render() {
        return (
            <nav className="navbar navbar-default navbar-fixed-top">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a className="navbar-brand" href="#"><img alt="Brand" height={20} width={20} src="/images/brand.jpg"></img>Discipline</a>
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
                            <UserDropdown user={this.state.user}/>
                        </ul>
                    </div>
                </div>
            </nav>
        );
    }
}

class UserDropdown extends React.Component {
    constructor(props) {
        super(props);
    }

    static signOff(e) {
        e.preventDefault();
        client({
            method: 'POST',
            path: '/logout',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        });
        window.location.href = '/login?logout';
    }

    render() {
        var userName;
        var fullName;
        if (this.props.user) {
            userName = this.props.user.username;
            fullName = this.props.user.firstName + ' ' + this.props.user.secondName;
        }
        return (
            <li className="dropdown">
                <a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">{userName} <span className="caret"></span></a>
                <ul className="dropdown-menu">
                    <li><a href="/user">{fullName}</a></li>
                    <li role="separator" className="divider"></li>
                    <li><a href="#" onClick={UserDropdown.signOff}>Sign off</a></li>
                </ul>
            </li>
        );
    }
}