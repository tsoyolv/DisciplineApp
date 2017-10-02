'use strict';

const React = require('react');

export default class Message extends React.Component {
    render() {
        // Was the message sent by the current user. If so, add a css class
        const fromMe = this.props.fromMe ? 'from-me' : '';

        return (
            <div className={`message ${fromMe}`}>
                <div className='username'>
                    { this.props.username }
                </div>
                <div className='username'>
                    {(new Date(this.props.wasSent)).toUTCString()}
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