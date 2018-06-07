import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IStreet } from 'app/shared/model/street.model';
import { getEntities as getStreets } from 'app/entities/street/street.reducer';
import { ILandProjects } from 'app/shared/model/land-projects.model';
import { getEntities as getLandProjects } from 'app/entities/land-projects/land-projects.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './house.reducer';
import { IHouse } from 'app/shared/model/house.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IHouseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IHouseUpdateState {
  isNew: boolean;
  cityId: number;
  streetId: number;
  projectId: number;
  createById: number;
  updateById: number;
}

export class HouseUpdate extends React.Component<IHouseUpdateProps, IHouseUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      cityId: 0,
      streetId: 0,
      projectId: 0,
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

    this.props.getCities();
    this.props.getStreets();
    this.props.getLandProjects();
    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { house } = this.props;
      const entity = {
        ...house,
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
    this.props.history.push('/entity/house');
  };

  cityUpdate = element => {
    const name = element.target.value.toString();
    if (name === '') {
      this.setState({
        cityId: -1
      });
    } else {
      for (const i in this.props.cities) {
        if (name === this.props.cities[i].name.toString()) {
          this.setState({
            cityId: this.props.cities[i].id
          });
        }
      }
    }
  };

  streetUpdate = element => {
    const name = element.target.value.toString();
    if (name === '') {
      this.setState({
        streetId: -1
      });
    } else {
      for (const i in this.props.streets) {
        if (name === this.props.streets[i].name.toString()) {
          this.setState({
            streetId: this.props.streets[i].id
          });
        }
      }
    }
  };

  projectUpdate = element => {
    const name = element.target.value.toString();
    if (name === '') {
      this.setState({
        projectId: -1
      });
    } else {
      for (const i in this.props.landProjects) {
        if (name === this.props.landProjects[i].name.toString()) {
          this.setState({
            projectId: this.props.landProjects[i].id
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
    const { house, cities, streets, landProjects, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.house.home.createOrEditLabel">
              <Translate contentKey="landexpApp.house.home.createOrEditLabel">Create or edit a House</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : house} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="house-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="actionTypeLabel">
                    <Translate contentKey="landexpApp.house.actionType">Action Type</Translate>
                  </Label>
                  <AvInput
                    id="house-actionType"
                    type="select"
                    className="form-control"
                    name="actionType"
                    value={(!isNew && house.actionType) || 'FOR_BUY'}
                  >
                    <option value="FOR_BUY">FOR_BUY</option>
                    <option value="FOR_SELL">FOR_SELL</option>
                    <option value="FOR_RENT">FOR_RENT</option>
                    <option value="FOR_HIRE">FOR_HIRE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="address">
                    <Translate contentKey="landexpApp.house.address">Address</Translate>
                  </Label>
                  <AvField id="house-address" type="text" name="address" />
                </AvGroup>
                <AvGroup>
                  <Label id="moneyLabel" for="money">
                    <Translate contentKey="landexpApp.house.money">Money</Translate>
                  </Label>
                  <AvField id="house-money" type="number" className="form-control" name="money" />
                </AvGroup>
                <AvGroup>
                  <Label id="moneyTypeLabel">
                    <Translate contentKey="landexpApp.house.moneyType">Money Type</Translate>
                  </Label>
                  <AvInput
                    id="house-moneyType"
                    type="select"
                    className="form-control"
                    name="moneyType"
                    value={(!isNew && house.moneyType) || 'MILLION'}
                  >
                    <option value="MILLION">MILLION</option>
                    <option value="HUNDRED_MILLION">HUNDRED_MILLION</option>
                    <option value="BILLION">BILLION</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="acreageLabel" for="acreage">
                    <Translate contentKey="landexpApp.house.acreage">Acreage</Translate>
                  </Label>
                  <AvField id="house-acreage" type="number" className="form-control" name="acreage" />
                </AvGroup>
                <AvGroup>
                  <Label id="discountLabel" for="discount">
                    <Translate contentKey="landexpApp.house.discount">Discount</Translate>
                  </Label>
                  <AvField id="house-discount" type="number" className="form-control" name="discount" />
                </AvGroup>
                <AvGroup>
                  <Label id="directionLabel">
                    <Translate contentKey="landexpApp.house.direction">Direction</Translate>
                  </Label>
                  <AvInput
                    id="house-direction"
                    type="select"
                    className="form-control"
                    name="direction"
                    value={(!isNew && house.direction) || 'NORTH'}
                  >
                    <option value="NORTH">NORTH</option>
                    <option value="SOUTH">SOUTH</option>
                    <option value="EAST">EAST</option>
                    <option value="WEST">WEST</option>
                    <option value="EAST_NORTH">EAST_NORTH</option>
                    <option value="WEST_NORTH">WEST_NORTH</option>
                    <option value="EAST_SOUTH">EAST_SOUTH</option>
                    <option value="WEST_SOUTH">WEST_SOUTH</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="directionBalconyLabel">
                    <Translate contentKey="landexpApp.house.directionBalcony">Direction Balcony</Translate>
                  </Label>
                  <AvInput
                    id="house-directionBalcony"
                    type="select"
                    className="form-control"
                    name="directionBalcony"
                    value={(!isNew && house.directionBalcony) || 'NORTH'}
                  >
                    <option value="NORTH">NORTH</option>
                    <option value="SOUTH">SOUTH</option>
                    <option value="EAST">EAST</option>
                    <option value="WEST">WEST</option>
                    <option value="EAST_NORTH">EAST_NORTH</option>
                    <option value="WEST_NORTH">WEST_NORTH</option>
                    <option value="EAST_SOUTH">EAST_SOUTH</option>
                    <option value="WEST_SOUTH">WEST_SOUTH</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="floorLabel" for="floor">
                    <Translate contentKey="landexpApp.house.floor">Floor</Translate>
                  </Label>
                  <AvField id="house-floor" type="text" name="floor" />
                </AvGroup>
                <AvGroup>
                  <Label id="numberOfFloorLabel" for="numberOfFloor">
                    <Translate contentKey="landexpApp.house.numberOfFloor">Number Of Floor</Translate>
                  </Label>
                  <AvField id="house-numberOfFloor" type="number" className="form-control" name="numberOfFloor" />
                </AvGroup>
                <AvGroup>
                  <Label id="bathRoomLabel" for="bathRoom">
                    <Translate contentKey="landexpApp.house.bathRoom">Bath Room</Translate>
                  </Label>
                  <AvField id="house-bathRoom" type="number" className="form-control" name="bathRoom" />
                </AvGroup>
                <AvGroup>
                  <Label id="parkingLabel" check>
                    <AvInput id="house-parking" type="checkbox" className="form-control" name="parking" />
                    <Translate contentKey="landexpApp.house.parking">Parking</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="furnitureLabel" check>
                    <AvInput id="house-furniture" type="checkbox" className="form-control" name="furniture" />
                    <Translate contentKey="landexpApp.house.furniture">Furniture</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="bedRoomLabel" for="bedRoom">
                    <Translate contentKey="landexpApp.house.bedRoom">Bed Room</Translate>
                  </Label>
                  <AvField id="house-bedRoom" type="number" className="form-control" name="bedRoom" />
                </AvGroup>
                <AvGroup>
                  <Label id="landTypeLabel">
                    <Translate contentKey="landexpApp.house.landType">Land Type</Translate>
                  </Label>
                  <AvInput
                    id="house-landType"
                    type="select"
                    className="form-control"
                    name="landType"
                    value={(!isNew && house.landType) || 'APARTMENT'}
                  >
                    <option value="APARTMENT">APARTMENT</option>
                    <option value="PEN_HOUSE">PEN_HOUSE</option>
                    <option value="HOME">HOME</option>
                    <option value="HOME_VILLA">HOME_VILLA</option>
                    <option value="HOME_STREET_SIDE">HOME_STREET_SIDE</option>
                    <option value="MOTEL_ROOM">MOTEL_ROOM</option>
                    <option value="OFFICE">OFFICE</option>
                    <option value="LAND_SCAPE">LAND_SCAPE</option>
                    <option value="LAND_OF_PROJECT">LAND_OF_PROJECT</option>
                    <option value="LAND_FARM">LAND_FARM</option>
                    <option value="LAND_RESORT">LAND_RESORT</option>
                    <option value="WAREHOUSES">WAREHOUSES</option>
                    <option value="KIOSKS">KIOSKS</option>
                    <option value="OTHER">OTHER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="saleTypeLabel">
                    <Translate contentKey="landexpApp.house.saleType">Sale Type</Translate>
                  </Label>
                  <AvInput
                    id="house-saleType"
                    type="select"
                    className="form-control"
                    name="saleType"
                    value={(!isNew && house.saleType) || 'SALE_BY_MYSELF'}
                  >
                    <option value="SALE_BY_MYSELF">SALE_BY_MYSELF</option>
                    <option value="SALE_SUPPORT">SALE_SUPPORT</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="feeLabel" for="fee">
                    <Translate contentKey="landexpApp.house.fee">Fee</Translate>
                  </Label>
                  <AvField id="house-fee" type="number" className="form-control" name="fee" />
                </AvGroup>
                <AvGroup>
                  <Label id="feeMaxLabel" for="feeMax">
                    <Translate contentKey="landexpApp.house.feeMax">Fee Max</Translate>
                  </Label>
                  <AvField id="house-feeMax" type="number" className="form-control" name="feeMax" />
                </AvGroup>
                <AvGroup>
                  <Label id="hitsLabel" for="hits">
                    <Translate contentKey="landexpApp.house.hits">Hits</Translate>
                  </Label>
                  <AvField id="house-hits" type="number" className="form-control" name="hits" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusTypeLabel">
                    <Translate contentKey="landexpApp.house.statusType">Status Type</Translate>
                  </Label>
                  <AvInput
                    id="house-statusType"
                    type="select"
                    className="form-control"
                    name="statusType"
                    value={(!isNew && house.statusType) || 'PENDING'}
                  >
                    <option value="PENDING">PENDING</option>
                    <option value="PAID">PAID</option>
                    <option value="APPROVED">APPROVED</option>
                    <option value="CANCELED">CANCELED</option>
                    <option value="EXPIRED">EXPIRED</option>
                    <option value="SOLD">SOLD</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.house.createAt">Create At</Translate>
                  </Label>
                  <AvField id="house-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateAtLabel" for="updateAt">
                    <Translate contentKey="landexpApp.house.updateAt">Update At</Translate>
                  </Label>
                  <AvField id="house-updateAt" type="date" className="form-control" name="updateAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="city.name">
                    <Translate contentKey="landexpApp.house.city">City</Translate>
                  </Label>
                  <AvInput id="house-city" type="select" className="form-control" name="cityId" onChange={this.cityUpdate}>
                    <option value="" key="0" />
                    {cities
                      ? cities.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="street.name">
                    <Translate contentKey="landexpApp.house.street">Street</Translate>
                  </Label>
                  <AvInput id="house-street" type="select" className="form-control" name="streetId" onChange={this.streetUpdate}>
                    <option value="" key="0" />
                    {streets
                      ? streets.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="project.name">
                    <Translate contentKey="landexpApp.house.project">Project</Translate>
                  </Label>
                  <AvInput id="house-project" type="select" className="form-control" name="projectId" onChange={this.projectUpdate}>
                    <option value="" key="0" />
                    {landProjects
                      ? landProjects.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="createBy.login">
                    <Translate contentKey="landexpApp.house.createBy">Create By</Translate>
                  </Label>
                  <AvInput id="house-createBy" type="select" className="form-control" name="createById" onChange={this.createByUpdate}>
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
                    <Translate contentKey="landexpApp.house.updateBy">Update By</Translate>
                  </Label>
                  <AvInput id="house-updateBy" type="select" className="form-control" name="updateById" onChange={this.updateByUpdate}>
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
                <Button tag={Link} id="cancel-save" to="/entity/house" replace color="info">
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
  cities: storeState.city.entities,
  streets: storeState.street.entities,
  landProjects: storeState.landProjects.entities,
  users: storeState.userManagement.users,
  house: storeState.house.entity,
  loading: storeState.house.loading,
  updating: storeState.house.updating
});

const mapDispatchToProps = {
  getCities,
  getStreets,
  getLandProjects,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HouseUpdate);
