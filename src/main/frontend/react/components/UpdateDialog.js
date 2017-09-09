'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

export default class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var updatedHabit = {};
        this.props.attributes.forEach(attribute => {
            updatedHabit[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.updatedEntity, updatedHabit);
        var dialogId = this.getDialogId();
        $('#' + dialogId).modal('hide');
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
                <input type="text" placeholder={attribute}
                       defaultValue={this.props.updatedEntity.entity[attribute]}
                       ref={attribute} className="field"/>
            </p>
        );

        var href = this.props.updatedEntity.entity._links.self.href;
        var dialogId = this.getDialogId();

        return (
            <div key={href}>
                <a href="#" data-toggle="modal" data-target={'#' + dialogId}>Update</a>
                <div className="modal fade" id={dialogId} tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
            </div>
        )
    }

    getDialogId() {
        var href = this.props.updatedEntity.entity._links.self.href;
         // entity id
        return "updateHabit-" + href.substr(href.lastIndexOf('\\'));
    }
}