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
import { IRegion } from 'app/shared/model/region.model';
import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-region.reducer';
import { IUserRegion } from 'app/shared/model/user-region.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IUserRegionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IUserRegionUpdateState {
  isNew: boolean;
  idsregion: any[];
  userId: number;
}

export class UserRegionUpdate extends React.Component<IUserRegionUpdateProps, IUserRegionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsregion: [],
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

    this.props.getUsers();
    this.props.getRegions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userRegion } = this.props;
      const entity = {
        ...userRegion,
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
    this.props.history.push('/entity/user-region');
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

  regionUpdate = element => {
    const selected = Array.from(element.target.selectedOptions).map((e: any) => e.value);
    this.setState({
      idsregion: keysToValues(selected, this.props.regions, 'name')
    });
  };

  displayregion(value: any) {
    if (this.state.idsregion && this.state.idsregion.length !== 0) {
      const list = [];
      for (const i in this.state.idsregion) {
        if (this.state.idsregion[i]) {
          list.push(this.state.idsregion[i].name);
        }
      }
      return list;
    }
    if (value.regions && value.regions.length !== 0) {
      const list = [];
      for (const i in value.regions) {
        if (value.regions[i]) {
          list.push(value.regions[i].name);
        }
      }
      this.setState({
        idsregion: keysToValues(list, this.props.regions, 'name')
      });
      return list;
    }
    return null;
  }

  render() {
    const isInvalid = false;
    const { userRegion, users, regions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.userRegion.home.createOrEditLabel">
              <Translate contentKey="landexpApp.userRegion.home.createOrEditLabel">Create or edit a UserRegion</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userRegion} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="user-region-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.userRegion.createAt">Create At</Translate>
                  </Label>
                  <AvField id="user-region-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateAtLabel" for="updateAt">
                    <Translate contentKey="landexpApp.userRegion.updateAt">Update At</Translate>
                  </Label>
                  <AvField id="user-region-updateAt" type="date" className="form-control" name="updateAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="landexpApp.userRegion.user">User</Translate>
                  </Label>
                  <AvInput id="user-region-user" type="select" className="form-control" name="userId" onChange={this.userUpdate}>
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
                  <Label for="regions">
                    <Translate contentKey="landexpApp.userRegion.region">Region</Translate>
                  </Label>
                  <AvInput
                    id="user-region-region"
                    type="select"
                    multiple
                    className="form-control"
                    name="fakeregions"
                    value={this.displayregion(userRegion)}
                    onChange={this.regionUpdate}
                  >
                    <option value="" key="0" />
                    {regions
                      ? regions.map(otherEntity => (
                          <option value={otherEntity.name} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvInput id="user-region-region" type="hidden" name="regions" value={this.state.idsregion} />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/user-region" replace color="info">
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
  regions: storeState.region.entities,
  userRegion: storeState.userRegion.entity,
  loading: storeState.userRegion.loading,
  updating: storeState.userRegion.updating
});

const mapDispatchToProps = {
  getUsers,
  getRegions,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserRegionUpdate);
