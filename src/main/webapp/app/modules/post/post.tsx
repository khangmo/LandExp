import './post.css';

import * as React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { Steps, Button, message } from 'antd';
const Step = Steps.Step;

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

import { getEntity, updateEntity } from './house.reducer';

import StepOne from './stepOne';
import StepTwo from './stepTwo';
import StepThree from './stepThree';
import StepFour from './stepFour';
import StepFive from './stepFive';
import StepSix from './stepSix';
import StepSeven from './stepSeven';

export interface IPostProp extends StateProps, DispatchProps {}

export interface IPostState {
  current: any;
  house: any;
}

export class PostPage extends React.Component<IPostProp, IPostState> {
  state: IPostState = {
    current: 0,
    house: {}
  };

  componentDidMount() {
    this.props.getSession();
    this.props.getEntity('init');
  }

  next = () => {
    this.validateStep(this.state.current);
    const current = this.state.current + 1;
    this.setState({ current });
  }

  prev = () => {
    const current = this.state.current - 1;
    this.setState({ current });
  }

  validateStep = id => {
    switch (id) {
      case 0:
        this.validateStepOne();
        break;
      case 1:
        this.validateStepTwo();
        break;
      default:
        break;
    }
  }

  validateStepOne = () => {
    // console.log('Validate step one');
  }

  validateStepTwo = () => {
    // console.log('Validate step two');
  }

  saveEntity = () => {
    const { house } = this.props;
    const entity = {
      ...house,
      ...this.state.house
    };
    console.log('Update entity', entity);
    this.props.updateEntity(entity);
    this.next();
  };

  updateHouse = house => {
    const nextHouse = { ...this.state.house, ...house };
    console.log('The next house', nextHouse);
    this.setState({
      house: nextHouse
    });
  }
  render() {
    const { current, house } = this.state;
    const steps = [
      {
        title: 'Hình thức',
        content: <StepOne updateHouse={this.updateHouse} />
      },
      {
        title: 'Vị trí',
        content: <StepTwo updateHouse={this.updateHouse} />
      },
      {
        title: 'Đặc điểm',
        content: <StepThree updateHouse={this.updateHouse} />
      },
      {
        title: 'Hình ảnh',
        content: <StepFour updateHouse={this.updateHouse} />
      },
      {
        title: 'Giá',
        content: <StepFive updateHouse={this.updateHouse} />
      },
      {
        title: 'Liên hệ',
        content: <StepSix updateHouse={this.updateHouse} />
      },
      {
        title: 'Xác nhận',
        content: <StepSeven />
      },
      {
        title: 'Thanh toán',
        content: <StepSeven />
      }
    ];
    return (
      <Row>
        <Col md="12">
          <Steps size="small" current={current}>
            {steps.map(item => <Step key={item.title} title={item.title} />)}
          </Steps>
        </Col>
        <Col md="12">
          <div>
            <div className="steps-content">{steps[this.state.current].content}</div>
            <div className="steps-action" style={{ marginTop: 16 }}>
              {this.state.current > 0 && <Button style={{ marginRight: 8 }} onClick={() => this.prev()}>Quay lại</Button>}
              {this.state.current < steps.length - 3 && (
                <Button type="primary" onClick={() => this.next()}>
                  Tiếp tục
                </Button>
              )}
              {this.state.current === steps.length - 3 && (
                <Button type="primary" onClick={() => this.saveEntity()}>
                  Hoàn tất
                </Button>
              )}
              {this.state.current === steps.length - 2 && (
                <Button type="primary" onClick={() => this.next()}>
                  Thanh toán
                </Button>
              )}
              {this.state.current === steps.length - 1 && (
                <Button type="primary" onClick={() => this.saveEntity()}>
                  Done
                </Button>
              )}
            </div>
          </div>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  house: storeState.house.entity,
  loading: storeState.house.loading,
  updating: storeState.house.updating
});

const mapDispatchToProps = { getSession, getEntity, updateEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PostPage);
