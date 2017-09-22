'use strict';

const React = require('react');
const client = require('./modules/client');
const stompClient = require('./modules/websocket-listener');

import Navbar from './components/Navbar'

export default class ChallengePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}, origChallenge:{}};
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

        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/challenges/' + id,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({origChallenge:response.entity});});
    }

    render () {
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
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
                                <li><a href="/challenge">Challenges</a></li>
                                <li><a href="#">Competition (Not implemented)</a></li>
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            <Challenge challenge={this.state.origChallenge}/>
                            <ChatDiv origChallenge={this.state.origChallenge}/>
                            <UserChallengesTable origchallenge={this.state.origChallenge} completed="false" title="User challenges"/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class UserChallengesTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {challenges: []};
        this.loadChallenges = this.loadChallenges.bind(this);
    }

    componentDidMount() {
        this.loadChallenges();
        stompClient.register([
            {route: '/topic/newUserChallenge', callback: this.loadChallenges},
            /*{route: '/topic/updateHabit', callback: this.refreshCurrentPage},
             {route: '/topic/deleteHabit', callback: this.refreshCurrentPage}*/
        ]);
    }

    loadChallenges() {
        var par = {};
        if (this.props.completed) {
            par = {completed: this.props.completed}
        }
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/challenges/' + id + '/userchallenges', /* /challenges/{challengeId}/userchallenges */
            params: par,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {this.setState({challenges:response.entity._embedded.items});});
    }

    render () {
        var outs = this.state.challenges.map(it => <UserChallenge key={it._links.self.href} challenge={it}/>);
        return (
            <table className="table table-hover">
                <caption>{this.props.title}</caption>
                <thead>
                <tr>
                    <th>User name</th>
                    <th>Progress</th>
                    <th>Updated Date</th>
                    <th>Votes</th>
                    <th>Vote</th>
                </tr>
                </thead>
                <tbody>
                {outs}
                </tbody>
            </table>
        );
    }
}

class UserChallenge extends React.Component {
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

class Challenge extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        return (
            <div><h1 className="page-header">{this.props.challenge.name}</h1>
                <h4>{this.props.challenge.difficulty}</h4>
                <span className="text-muted">Difficulty</span>
                <h4>{this.props.challenge.description}</h4>
                <span className="text-muted">Description</span>
                <h4>{this.props.challenge.votes}</h4>
                <span className="text-muted">Votes</span>
            </div>
        );
    }
}

class ChatDiv extends React.Component {
    constructor(props) {
        super(props);
        this.handleAccept = this.handleAccept.bind(this);
    }

    handleAccept() {
        if(confirm('Accept the challenge?')) {
            client({
                method: 'PUT',
                path: this.props.origChallenge._links.accept.href,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
                }
            }).done(response => {
                    if (response.status.code === 200) {
                        //this.setState({alert:{entity:response.entity, message:'Deletion successful'}})
                    }
                    /* let the websocket handle updating the UI */},
                response => {
                    if (response.status.code === 403) {
                        alert('ACCESS DENIED: You are not authorized to delete ' +
                            habit.entity._links.self.href);
                    }
                });
        }
    }

    render () {
        if (this.props.origChallenge.acceptableForCurrentUser != undefined && !this.props.origChallenge.acceptableForCurrentUser) {
            return <ChatApp/>
        } else {
            return <h2>Chat is locked. Accept the challenge for unlock. <button className="btn btn-lg btn-primary" onClick={this.handleAccept}>Accept</button></h2>
        }
    }
}

class ChatApp extends React.Component {
    //socket = {};
    constructor(props) {
        super(props);
        this.state = { messages: [] };
        this.sendHandler = this.sendHandler.bind(this);

        // Connect to the server
    //    this.socket = io(config.api, { query: `username=${props.username}` }).connect();

        // Listen for messages from the server
        /*this.socket.on('server:message', message => {
            this.addMessage(message);
        });*/
    }

    sendHandler(message) {
        const messageObject = {
            username: this.props.username,
            message
        };

        // Emit the message to the server
       // this.socket.emit('client:message', messageObject);

        messageObject.fromMe = true;
        this.addMessage(messageObject);
    }

    addMessage(message) {
        // Append the message to the component state
        const messages = this.state.messages;
        messages.push(message);
        this.setState({ messages });
    }

    render() {
        return (
            <div className="container">
                <h3>React Chat App</h3>
                <Messages messages={this.state.messages} />
                <ChatInput onSend={this.sendHandler} />
            </div>
        );
    }

}
ChatApp.defaultProps = {
    username: 'Anonymous'
};

class Messages extends React.Component {
    componentDidUpdate() {
        // There is a new message in the state, scroll to bottom of list
        const objDiv = document.getElementById('messageList');
        objDiv.scrollTop = objDiv.scrollHeight;
    }

    render() {
        // Loop through all the messages in the state and create a Message component
        const messages = this.props.messages.map((message, i) => {
            return (
                <Message
                    key={i}
                    username={message.username}
                    message={message.message}
                    fromMe={message.fromMe} />
            );
        });

        return (
            <div className='messages' id='messageList'>
                { messages }
            </div>
        );
    }
}

Messages.defaultProps = {
    messages: []
};

class Message extends React.Component {
    render() {
        // Was the message sent by the current user. If so, add a css class
        const fromMe = this.props.fromMe ? 'from-me' : '';

        return (
            <div className={`message ${fromMe}`}>
                <div className='username'>
                    { this.props.username }
                </div>
                <div className='message-body'>
                    { this.props.message }
                </div>
            </div>
        );
    }
}

Message.defaultProps = {
    message: '',
    username: '',
    fromMe: false
};

class ChatInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = { chatInput: '' };

        // React ES6 does not bind 'this' to event handlers by default
        this.submitHandler = this.submitHandler.bind(this);
        this.textChangeHandler = this.textChangeHandler.bind(this);
    }

    submitHandler(event) {
        // Stop the form from refreshing the page on submit
        event.preventDefault();

        // Clear the input box
        this.setState({ chatInput: '' });

        // Call the onSend callback with the chatInput message
        this.props.onSend(this.state.chatInput);
    }

    textChangeHandler(event)  {
        this.setState({ chatInput: event.target.value });
    }

    render() {
        return (
            <form className="chat-input" onSubmit={this.submitHandler}>
                <input type="text"
                       onChange={this.textChangeHandler}
                       value={this.state.chatInput}
                       placeholder="Write a message..."
                       required />
            </form>
        );
    }
}

ChatInput.defaultProps = {
};
