import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { View, requireNativeComponent, DeviceEventEmitter, ViewPropTypes } from 'react-native';


class CircleSeeker extends Component {
    componentDidMount() {
        DeviceEventEmitter.addListener('onChangeCircleSeeker', (message) => {
            if (this.props.onChange) {
                this.props.onChange(message)
            }

        });
        DeviceEventEmitter.addListener('onUpCircleSeeker', (message) => {
            if (this.props.onPressOut) {
                this.props.onPressOut(message)
            }
        });
        DeviceEventEmitter.addListener('onDownCircleSeeker', (message) => {
            if (this.props.onPressIn) {
                this.props.onPressIn(message)
            }
        });
    }

    componentWillUnmount() {
        DeviceEventEmitter.removeAllListeners('onChangeCircleSeeker');
        DeviceEventEmitter.removeAllListeners('onUpCircleSeeker');
        DeviceEventEmitter.removeAllListeners('onDownCircleSeeker');
    }

    setNativeProps = (nativeProps) => {
        this._root.setNativeProps(nativeProps);
    }

    setValue = (value) => {
        this.setNativeProps({ value: value })
    }

    render() {

        return (
            <CircleSeekerView
                ref={ref => this._root = ref}
                {...this.props}
            />
        );
    }
}

CircleSeeker.propTypes = {
    ...(ViewPropTypes || View.propTypes),
    enable: PropTypes.bool,
    withLine: PropTypes.number,
    withLineBackground: PropTypes.number,
    colorPoint: PropTypes.string,
    colorCircleBackground: PropTypes.string,
    colorCircle: PropTypes.string,
    value: PropTypes.number,
    onChange: PropTypes.func,
    onPressOut: PropTypes.func,
    onPressIn: PropTypes.func,
};

CircleSeeker.defaultProps = {
    enable: true,
    withLine: 10,
    withLineBackground: 10,
    colorPoint: 'red',
    colorCircleBackground: 'gray',
    colorCircle: 'green',
};

const CircleSeekerView = requireNativeComponent('CircleSeeker', CircleSeeker);

module.exports = CircleSeeker;
