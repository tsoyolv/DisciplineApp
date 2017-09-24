'use strict';

const React = require('react');

import Message from './Message'

export default class Messages extends React.Component {
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
                    fromMe={message.fromMe}
                    wasSent={message.wasSent}
                />
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