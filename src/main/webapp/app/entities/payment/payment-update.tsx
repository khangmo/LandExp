import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IHouse } from 'app/shared/model/house.model';
import { getEntities as getHouses } from 'app/entities/house/house.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment.reducer';
import { IPayment } from 'app/shared/model/payment.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IPaymentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IPaymentUpdateState {
  isNew: boolean;
  houseId: number;
  customerId: number;
  createById: number;
  updateById: number;
}

export class PaymentUpdate extends React.Component<IPaymentUpdateProps, IPaymentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      houseId: 0,
      customerId: 0,
      createById: 0,
      updateById: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getHouses();
    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { payment } = this.props;
      const entity = {
        ...payment,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/payment');
  };

  houseUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        houseId: -1
      });
    } else {
      for (const i in this.props.houses) {
        if (id === this.props.houses[i].id.toString()) {
          this.setState({
            houseId: this.props.houses[i].id
          });
        }
      }
    }
  };

  customerUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        customerId: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            customerId: this.props.users[i].id
          });
        }
      }
    }
  };

  createByUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        createById: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            createById: this.props.users[i].id
          });
        }
      }
    }
  };

  updateByUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        updateById: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            updateById: this.props.users[i].id
          });
        }
      }
    }
  };

  render() {
    const isInvalid = false;
    const { payment, houses, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.payment.home.createOrEditLabel">
              <Translate contentKey="landexpApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : payment} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="payment-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="codeLabel" for="code">
                    <Translate contentKey="landexpApp.payment.code">Code</Translate>
                  </Label>
                  <AvField id="payment-code" type="text" name="code" />
                </AvGroup>
                <AvGroup>
                  <Label id="moneyLabel" for="money">
                    <Translate contentKey="landexpApp.payment.money">Money</Translate>
                  </Label>
                  <AvField id="payment-money" type="number" className="form-control" name="money" />
                </AvGroup>
                <AvGroup>
                  <Label id="paidTimeLabel" for="paidTime">
                    <Translate contentKey="landexpApp.payment.paidTime">Paid Time</Translate>
                  </Label>
                  <AvField id="payment-paidTime" type="date" className="form-control" name="paidTime" />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentStatusLabel">
                    <Translate contentKey="landexpApp.payment.paymentStatus">Payment Status</Translate>
                  </Label>
                  <AvInput
                    id="payment-paymentStatus"
                    type="select"
                    className="form-control"
                    name="paymentStatus"
                    value={(!isNew && payment.paymentStatus) || 'OPEN'}
                  >
                    <option value="OPEN">OPEN</option>
                    <option value="PENDING">PENDING</option>
                    <option value="SUCCESS">SUCCESS</option>
                    <option value="FAILED">FAILED</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.payment.createAt">Create At</Translate>
                  </Label>
                  <AvField id="payment-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateAtLabel" for="updateAt">
                    <Translate contentKey="landexpApp.payment.updateAt">Update At</Translate>
                  </Label>
                  <AvField id="payment-updateAt" type="date" className="form-control" name="updateAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="house.id">
                    <Translate contentKey="landexpApp.payment.house">House</Translate>
                  </Label>
                  <AvInput id="payment-house" type="select" className="form-control" name="houseId" onChange={this.houseUpdate}>
                    <option value="" key="0" />
                    {houses
                      ? houses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="customer.login">
                    <Translate contentKey="landexpApp.payment.customer">Customer</Translate>
                  </Label>
                  <AvInput id="payment-customer" type="select" className="form-control" name="customerId" onChange={this.customerUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="createBy.login">
                    <Translate contentKey="landexpApp.payment.createBy">Create By</Translate>
                  </Label>
                  <AvInput id="payment-createBy" type="select" className="form-control" name="createById" onChange={this.createByUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="updateBy.login">
                    <Translate contentKey="landexpApp.payment.updateBy">Update By</Translate>
                  </Label>
                  <AvInput id="payment-updateBy" type="select" className="form-control" name="updateById" onChange={this.updateByUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/payment" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={isInvalid || updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  houses: storeState.house.entities,
  users: storeState.userManagement.users,
  payment: storeState.payment.entity,
  loading: storeState.payment.loading,
  updating: storeState.payment.updating
});

const mapDispatchToProps = {
  getHouses,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentUpdate);
