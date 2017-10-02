'use strict';

const React = require('react');
const client = require('./../modules/client');

export default class UserChallengeTableRow extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}};
        this.handleVote = this.handleVote.bind(this);
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: this.props.challenge._links.challengeUser.href,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({user:response.entity});});
    }

    handleVote() {
        /*client({
         method: 'PUT',
         path: habit.entity._links.self.href,
         headers: {
         'Content-Type': 'application/json',
         'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
         }
         }).done(response => {
         if (response.status.code === 204) {
         this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
         }
         /!* let the websocket handle updating the UI *!/},
         response => {
         if (response.status.code === 403) {
         alert('ACCESS DENIED: You are not authorized to delete ' +
         habit.entity._links.self.href);
         }
         });*/
    }

    render () {
        var userlink = '#';
        if (this.state.user._links) {
            userlink = this.state.user._links.self.href;
        }
        return (<tr>
                <td><a href={userlink}>{this.state.user.username}</a></td>  {/*<a href={this.props.challenge._links.link.href}>{this.props.challenge.name}</a>*/}
                <td>
                    <div className="progress">
                        <div style={{width: '60%'}} aria-valuemax="100" aria-valuemin="0" aria-valuenow="60" role="progressbar" className="red progress-bar">
                            <span>60%</span>
                        </div>
                    </div>
                </td>
                <td>{(new Date(this.props.challenge.updatedWhen)).toUTCString()}</td>
                <td>{this.props.challenge.votes}</td>
                <td><button className="btn btn-lg btn-primary btn-block" onClick={this.handleVote}>Vote</button></td>
            </tr>
        );
    }
}