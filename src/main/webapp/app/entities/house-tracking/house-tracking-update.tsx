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
import { getEntity, updateEntity, createEntity, reset } from './house-tracking.reducer';
import { IHouseTracking } from 'app/shared/model/house-tracking.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IHouseTrackingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IHouseTrackingUpdateState {
  isNew: boolean;
  houseId: number;
  userId: number;
}

export class HouseTrackingUpdate extends React.Component<IHouseTrackingUpdateProps, IHouseTrackingUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      houseId: 0,
      userId: 0,
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
      const { houseTracking } = this.props;
      const entity = {
        ...houseTracking,
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
    this.props.history.push('/entity/house-tracking');
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

  userUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        userId: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            userId: this.props.users[i].id
          });
        }
      }
    }
  };

  render() {
    const isInvalid = false;
    const { houseTracking, houses, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.houseTracking.home.createOrEditLabel">
              <Translate contentKey="landexpApp.houseTracking.home.createOrEditLabel">Create or edit a HouseTracking</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : houseTracking} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="house-tracking-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="activityTypeLabel">
                    <Translate contentKey="landexpApp.houseTracking.activityType">Activity Type</Translate>
                  </Label>
                  <AvInput
                    id="house-tracking-activityType"
                    type="select"
                    className="form-control"
                    name="activityType"
                    value={(!isNew && houseTracking.activityType) || 'USER_SEARCH_BUY'}
                  >
                    <option value="USER_SEARCH_BUY">USER_SEARCH_BUY</option>
                    <option value="USER_SEARCH_RENT">USER_SEARCH_RENT</option>
                    <option value="USER_VIEW_NEWS">USER_VIEW_NEWS</option>
                    <option value="USER_LIKE_NEWS">USER_LIKE_NEWS</option>
                    <option value="USER_CREATE_NEWS">USER_CREATE_NEWS</option>
                    <option value="USER_UPDATE_NEWS">USER_UPDATE_NEWS</option>
                    <option value="USER_SUBSCRIPTION">USER_SUBSCRIPTION</option>
                    <option value="USER_UNSUBSCRIPTION">USER_UNSUBSCRIPTION</option>
                    <option value="USER_REGISTERED_CONSULTING">USER_REGISTERED_CONSULTING</option>
                    <option value="USER_UPDATE_SALE_TYPE">USER_UPDATE_SALE_TYPE</option>
                    <option value="USER_PAID_NEWS">USER_PAID_NEWS</option>
                    <option value="USER_SOLD_HOUSE">USER_SOLD_HOUSE</option>
                    <option value="USER_BOUGTH_HOUSE">USER_BOUGTH_HOUSE</option>
                    <option value="USER_CREATE_BANNER">USER_CREATE_BANNER</option>
                    <option value="USER_UPDATE_BANNER">USER_UPDATE_BANNER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="sourceIdLabel" for="sourceId">
                    <Translate contentKey="landexpApp.houseTracking.sourceId">Source Id</Translate>
                  </Label>
                  <AvField id="house-tracking-sourceId" type="text" name="sourceId" />
                </AvGroup>
                <AvGroup>
                  <Label id="sourceLinkLabel" for="sourceLink">
                    <Translate contentKey="landexpApp.houseTracking.sourceLink">Source Link</Translate>
                  </Label>
                  <AvField id="house-tracking-sourceLink" type="text" name="sourceLink" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="landexpApp.houseTracking.description">Description</Translate>
                  </Label>
                  <AvField id="house-tracking-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.houseTracking.createAt">Create At</Translate>
                  </Label>
                  <AvField id="house-tracking-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="house.id">
                    <Translate contentKey="landexpApp.houseTracking.house">House</Translate>
                  </Label>
                  <AvInput id="house-tracking-house" type="select" className="form-control" name="houseId" onChange={this.houseUpdate}>
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
                  <Label for="user.login">
                    <Translate contentKey="landexpApp.houseTracking.user">User</Translate>
                  </Label>
                  <AvInput id="house-tracking-user" type="select" className="form-control" name="userId" onChange={this.userUpdate}>
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
                <Button tag={Link} id="cancel-save" to="/entity/house-tracking" replace color="info">
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
  houseTracking: storeState.houseTracking.entity,
  loading: storeState.houseTracking.loading,
  updating: storeState.houseTracking.updating
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

export default connect(mapStateToProps, mapDispatchToProps)(HouseTrackingUpdate);
