import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILandProject } from 'app/shared/model/land-project.model';
import { getEntities as getLandProjects } from 'app/entities/land-project/land-project.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './land-project-photo.reducer';
import { ILandProjectPhoto } from 'app/shared/model/land-project-photo.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ILandProjectPhotoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ILandProjectPhotoUpdateState {
  isNew: boolean;
  landProjectId: number;
  createById: number;
  updateById: number;
}

export class LandProjectPhotoUpdate extends React.Component<ILandProjectPhotoUpdateProps, ILandProjectPhotoUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      landProjectId: 0,
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

    this.props.getLandProjects();
    this.props.getUsers();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { landProjectPhoto } = this.props;
      const entity = {
        ...landProjectPhoto,
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
    this.props.history.push('/entity/land-project-photo');
  };

  landProjectUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        landProjectId: -1
      });
    } else {
      for (const i in this.props.landProjects) {
        if (id === this.props.landProjects[i].id.toString()) {
          this.setState({
            landProjectId: this.props.landProjects[i].id
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
    const { landProjectPhoto, landProjects, users, loading, updating } = this.props;
    const { isNew } = this.state;

    const { image, imageContentType } = landProjectPhoto;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.landProjectPhoto.home.createOrEditLabel">
              <Translate contentKey="landexpApp.landProjectPhoto.home.createOrEditLabel">Create or edit a LandProjectPhoto</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : landProjectPhoto} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="land-project-photo-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <AvGroup>
                    <Label id="imageLabel" for="image">
                      <Translate contentKey="landexpApp.landProjectPhoto.image">Image</Translate>
                    </Label>
                    <br />
                    {image ? (
                      <div>
                        <a onClick={openFile(imageContentType, image)}>
                          <img src={`data:${imageContentType};base64,${image}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {imageContentType}, {byteSize(image)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('image')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_image" type="file" onChange={this.onBlobChange(true, 'image')} accept="image/*" />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.landProjectPhoto.createAt">Create At</Translate>
                  </Label>
                  <AvField id="land-project-photo-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="landProject.id">
                    <Translate contentKey="landexpApp.landProjectPhoto.landProject">Land Project</Translate>
                  </Label>
                  <AvInput
                    id="land-project-photo-landProject"
                    type="select"
                    className="form-control"
                    name="landProjectId"
                    onChange={this.landProjectUpdate}
                  >
                    <option value="" key="0" />
                    {landProjects
                      ? landProjects.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="createBy.login">
                    <Translate contentKey="landexpApp.landProjectPhoto.createBy">Create By</Translate>
                  </Label>
                  <AvInput
                    id="land-project-photo-createBy"
                    type="select"
                    className="form-control"
                    name="createById"
                    onChange={this.createByUpdate}
                  >
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
                    <Translate contentKey="landexpApp.landProjectPhoto.updateBy">Update By</Translate>
                  </Label>
                  <AvInput
                    id="land-project-photo-updateBy"
                    type="select"
                    className="form-control"
                    name="updateById"
                    onChange={this.updateByUpdate}
                  >
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
                <Button tag={Link} id="cancel-save" to="/entity/land-project-photo" replace color="info">
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
  landProjects: storeState.landProject.entities,
  users: storeState.userManagement.users,
  landProjectPhoto: storeState.landProjectPhoto.entity,
  loading: storeState.landProjectPhoto.loading,
  updating: storeState.landProjectPhoto.updating
});

const mapDispatchToProps = {
  getLandProjects,
  getUsers,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LandProjectPhotoUpdate);
