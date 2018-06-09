import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IDistrict } from 'app/shared/model/district.model';
import { getEntities as getDistricts } from 'app/entities/district/district.reducer';
import { IStreet } from 'app/shared/model/street.model';
import { getEntities as getStreets } from 'app/entities/street/street.reducer';
import { getEntity, updateEntity, createEntity, reset } from './search-tracking.reducer';
import { ISearchTracking } from 'app/shared/model/search-tracking.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ISearchTrackingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ISearchTrackingUpdateState {
  isNew: boolean;
  userId: number;
  cityId: number;
  districtId: number;
  streetId: number;
}

export class SearchTrackingUpdate extends React.Component<ISearchTrackingUpdateProps, ISearchTrackingUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: 0,
      cityId: 0,
      districtId: 0,
      streetId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getCities();
    this.props.getDistricts();
    this.props.getStreets();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { searchTracking } = this.props;
      const entity = {
        ...searchTracking,
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
    this.props.history.push('/entity/search-tracking');
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

  districtUpdate = element => {
    const name = element.target.value.toString();
    if (name === '') {
      this.setState({
        districtId: -1
      });
    } else {
      for (const i in this.props.districts) {
        if (name === this.props.districts[i].name.toString()) {
          this.setState({
            districtId: this.props.districts[i].id
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

  render() {
    const isInvalid = false;
    const { searchTracking, users, cities, districts, streets, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.searchTracking.home.createOrEditLabel">
              <Translate contentKey="landexpApp.searchTracking.home.createOrEditLabel">Create or edit a SearchTracking</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : searchTracking} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="search-tracking-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="actionTypeLabel">
                    <Translate contentKey="landexpApp.searchTracking.actionType">Action Type</Translate>
                  </Label>
                  <AvInput
                    id="search-tracking-actionType"
                    type="select"
                    className="form-control"
                    name="actionType"
                    value={(!isNew && searchTracking.actionType) || 'USER_SEARCH_BUY'}
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
                  <Label id="keywordLabel" for="keyword">
                    <Translate contentKey="landexpApp.searchTracking.keyword">Keyword</Translate>
                  </Label>
                  <AvField id="search-tracking-keyword" type="text" name="keyword" />
                </AvGroup>
                <AvGroup>
                  <Label id="costFromLabel" for="costFrom">
                    <Translate contentKey="landexpApp.searchTracking.costFrom">Cost From</Translate>
                  </Label>
                  <AvField id="search-tracking-costFrom" type="number" className="form-control" name="costFrom" />
                </AvGroup>
                <AvGroup>
                  <Label id="costToLabel" for="costTo">
                    <Translate contentKey="landexpApp.searchTracking.costTo">Cost To</Translate>
                  </Label>
                  <AvField id="search-tracking-costTo" type="number" className="form-control" name="costTo" />
                </AvGroup>
                <AvGroup>
                  <Label id="acreageFromLabel" for="acreageFrom">
                    <Translate contentKey="landexpApp.searchTracking.acreageFrom">Acreage From</Translate>
                  </Label>
                  <AvField id="search-tracking-acreageFrom" type="number" className="form-control" name="acreageFrom" />
                </AvGroup>
                <AvGroup>
                  <Label id="acreageToLabel" for="acreageTo">
                    <Translate contentKey="landexpApp.searchTracking.acreageTo">Acreage To</Translate>
                  </Label>
                  <AvField id="search-tracking-acreageTo" type="number" className="form-control" name="acreageTo" />
                </AvGroup>
                <AvGroup>
                  <Label id="directionLabel">
                    <Translate contentKey="landexpApp.searchTracking.direction">Direction</Translate>
                  </Label>
                  <AvInput
                    id="search-tracking-direction"
                    type="select"
                    className="form-control"
                    name="direction"
                    value={(!isNew && searchTracking.direction) || 'NORTH'}
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
                    <Translate contentKey="landexpApp.searchTracking.floor">Floor</Translate>
                  </Label>
                  <AvField id="search-tracking-floor" type="text" name="floor" />
                </AvGroup>
                <AvGroup>
                  <Label id="bathRoomLabel" for="bathRoom">
                    <Translate contentKey="landexpApp.searchTracking.bathRoom">Bath Room</Translate>
                  </Label>
                  <AvField id="search-tracking-bathRoom" type="number" className="form-control" name="bathRoom" />
                </AvGroup>
                <AvGroup>
                  <Label id="bedRoomLabel" for="bedRoom">
                    <Translate contentKey="landexpApp.searchTracking.bedRoom">Bed Room</Translate>
                  </Label>
                  <AvField id="search-tracking-bedRoom" type="number" className="form-control" name="bedRoom" />
                </AvGroup>
                <AvGroup>
                  <Label id="parkingLabel" check>
                    <AvInput id="search-tracking-parking" type="checkbox" className="form-control" name="parking" />
                    <Translate contentKey="landexpApp.searchTracking.parking">Parking</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="landTypeLabel">
                    <Translate contentKey="landexpApp.searchTracking.landType">Land Type</Translate>
                  </Label>
                  <AvInput
                    id="search-tracking-landType"
                    type="select"
                    className="form-control"
                    name="landType"
                    value={(!isNew && searchTracking.landType) || 'APARTMENT'}
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
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.searchTracking.createAt">Create At</Translate>
                  </Label>
                  <AvField id="search-tracking-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="landexpApp.searchTracking.user">User</Translate>
                  </Label>
                  <AvInput id="search-tracking-user" type="select" className="form-control" name="userId" onChange={this.userUpdate}>
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
                  <Label for="city.name">
                    <Translate contentKey="landexpApp.searchTracking.city">City</Translate>
                  </Label>
                  <AvInput id="search-tracking-city" type="select" className="form-control" name="cityId" onChange={this.cityUpdate}>
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
                  <Label for="district.name">
                    <Translate contentKey="landexpApp.searchTracking.district">District</Translate>
                  </Label>
                  <AvInput
                    id="search-tracking-district"
                    type="select"
                    className="form-control"
                    name="districtId"
                    onChange={this.districtUpdate}
                  >
                    <option value="" key="0" />
                    {districts
                      ? districts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="street.name">
                    <Translate contentKey="landexpApp.searchTracking.street">Street</Translate>
                  </Label>
                  <AvInput id="search-tracking-street" type="select" className="form-control" name="streetId" onChange={this.streetUpdate}>
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
                <Button tag={Link} id="cancel-save" to="/entity/search-tracking" replace color="info">
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
  users: storeState.userManagement.users,
  cities: storeState.city.entities,
  districts: storeState.district.entities,
  streets: storeState.street.entities,
  searchTracking: storeState.searchTracking.entity,
  loading: storeState.searchTracking.loading,
  updating: storeState.searchTracking.updating
});

const mapDispatchToProps = {
  getUsers,
  getCities,
  getDistricts,
  getStreets,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SearchTrackingUpdate);
