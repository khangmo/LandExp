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
import { getEntity, updateEntity, createEntity, reset } from './banner.reducer';
import { IBanner } from 'app/shared/model/banner.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IBannerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IBannerUpdateState {
  isNew: boolean;
  createById: number;
  updateById: number;
}

export class BannerUpdate extends React.Component<IBannerUpdateProps, IBannerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { banner } = this.props;
      const entity = {
        ...banner,
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
    this.props.history.push('/entity/banner');
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
    const { banner, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.banner.home.createOrEditLabel">
              <Translate contentKey="landexpApp.banner.home.createOrEditLabel">Create or edit a Banner</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : banner} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="banner-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="title">
                    <Translate contentKey="landexpApp.banner.title">Title</Translate>
                  </Label>
                  <AvField id="banner-title" type="text" name="title" />
                </AvGroup>
                <AvGroup>
                  <Label id="titleAliasLabel" for="titleAlias">
                    <Translate contentKey="landexpApp.banner.titleAlias">Title Alias</Translate>
                  </Label>
                  <AvField id="banner-titleAlias" type="text" name="titleAlias" />
                </AvGroup>
                <AvGroup>
                  <Label id="areaLabel" for="area">
                    <Translate contentKey="landexpApp.banner.area">Area</Translate>
                  </Label>
                  <AvField id="banner-area" type="number" className="form-control" name="area" />
                </AvGroup>
                <AvGroup>
                  <Label id="hitsLabel" for="hits">
                    <Translate contentKey="landexpApp.banner.hits">Hits</Translate>
                  </Label>
                  <AvField id="banner-hits" type="number" className="form-control" name="hits" />
                </AvGroup>
                <AvGroup>
                  <Label id="publicUpLabel" for="publicUp">
                    <Translate contentKey="landexpApp.banner.publicUp">Public Up</Translate>
                  </Label>
                  <AvField id="banner-publicUp" type="date" className="form-control" name="publicUp" />
                </AvGroup>
                <AvGroup>
                  <Label id="publicDownLabel" for="publicDown">
                    <Translate contentKey="landexpApp.banner.publicDown">Public Down</Translate>
                  </Label>
                  <AvField id="banner-publicDown" type="date" className="form-control" name="publicDown" />
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.banner.createAt">Create At</Translate>
                  </Label>
                  <AvField id="banner-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateAtLabel" for="updateAt">
                    <Translate contentKey="landexpApp.banner.updateAt">Update At</Translate>
                  </Label>
                  <AvField id="banner-updateAt" type="date" className="form-control" name="updateAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="createBy.login">
                    <Translate contentKey="landexpApp.banner.createBy">Create By</Translate>
                  </Label>
                  <AvInput id="banner-createBy" type="select" className="form-control" name="createById" onChange={this.createByUpdate}>
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
                    <Translate contentKey="landexpApp.banner.updateBy">Update By</Translate>
                  </Label>
                  <AvInput id="banner-updateBy" type="select" className="form-control" name="updateById" onChange={this.updateByUpdate}>
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
                <Button tag={Link} id="cancel-save" to="/entity/banner" replace color="info">
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
  banner: storeState.banner.entity,
  loading: storeState.banner.loading,
  updating: storeState.banner.updating
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BannerUpdate);
