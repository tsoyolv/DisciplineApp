'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

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
        $('#' + this.props.modalId).modal('hide');
        window.location = "#"; // todo close modal dialog
    }

    render() {
        var inputs = this.props.attributes
            .map(attribute =>
                <p key={attribute}>
                    <input type="text" placeholder={attribute} ref={attribute} className="field"/>
                </p>
            );

        return (
        <div className="modal fade" id={this.props.modalId} tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div className="modal-dialog" role="document">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="exampleModalLabel">{this.props.titleName}</h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form>
                        <div className="modal-body">
                            {inputs}
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-primary" onClick={this.handleSubmit}>{this.props.buttonName}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        );
    }
}