'use strict';

const React = require('react');

export default class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var object = {};
        this.props.attributes.forEach(attribute => {
            object[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(object);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes
            .map(attribute =>
                <p key={attribute}>
                    <input type="text" placeholder={attribute} ref={attribute} className="field"/>
                </p>
            );

        return (
            <div id={this.props.modalId} className="modalDialog">
                <div>
                    <a href="#" title="Close" className="close">X</a>

                    <h2>{this.props.titleName}</h2>

                    <form>
                        {inputs}
                        <button onClick={this.handleSubmit}>{this.props.buttonName}</button>
                    </form>
                </div>
            </div>
        );
    }
}