'use strict';

const React = require('react');
const client = require('./../modules/client');
const stompClient = require('./../modules/websocket-listener');

import Messages from './Messages'

export default class ChatDiv extends React.Component {
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
            return <ChatApp challenge={this.props.origChallenge} user={this.props.user}/>
        } else {
            return <h2>Chat is locked. Accept the challenge for unlock. <button className="btn btn-lg btn-primary" onClick={this.handleAccept}>Accept</button></h2>
        }
    }
}

class ChatApp extends React.Component {

    constructor(props) {
        super(props);
        this.state = { messages: [] };
        this.sendHandler = this.sendHandler.bind(this);
        this.loadMessages = this.loadMessages.bind(this);
    }

    componentDidMount() {
        this.loadMessages();
        stompClient.register([
            {route: '/topic/newMessage', callback: this.loadMessages},
            /*{route: '/topic/updateHabit', callback: this.refreshCurrentPage},
             {route: '/topic/deleteHabit', callback: this.refreshCurrentPage}*/
        ]);
    }

    loadMessages() {
        client({
            method: 'GET',
            path: this.props.challenge._links.messages.href,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            var messages = response.entity._embedded.items;
            messages = messages.sort(function(a,b){
                return new Date(a.wasSent) - new Date(b.wasSent);
            });
            messages.forEach(m => {
                var href = m._links.self.href;
                var messageId = href.substr(href.lastIndexOf('/') + 1);
                const messageObject = {
                    username: m.username,
                    message: m.message,
                    wasSent: m.wasSent,
                    messageId:messageId
                };
                if (m.username == this.props.user.username) {
                    messageObject.fromMe = true;
                }
                this.addMessage(messageObject);
            });
        });
    }

    sendHandler(message) {
        const messageEntity = {
            message: message
        };

        var challengeHref = this.props.challenge._links.self.href;
        var challengeId = challengeHref.substr(challengeHref.lastIndexOf('/') + 1);
        var userHref = this.props.user._links.self.href;
        var userId = userHref.substr(userHref.lastIndexOf('/') + 1);
        client({
            method: 'POST',
            path: '/api/messages/send/' + challengeId + '/' + userId,
            entity: messageEntity,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {});
    }

    addMessage(message) {
        const messages = this.state.messages;
        if (messages.find(m => m.messageId == message.messageId)) {
            return;
        }
        messages.push(message);
        this.setState({ messages });
    }

    render() {
        return (
            <div className="container">
                <h3>Challenge chat</h3>
                <Messages messages={this.state.messages} />
                <ChatInput onSend={this.sendHandler} />
            </div>
        );
    }

}
ChatApp.defaultProps = {
    username: 'Anonymous'
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
