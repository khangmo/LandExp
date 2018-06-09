import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './house-photo.reducer';
import { IHousePhoto } from 'app/shared/model/house-photo.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHousePhotoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class HousePhotoDetail extends React.Component<IHousePhotoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { housePhoto } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.housePhoto.detail.title">HousePhoto</Translate> [<b>{housePhoto.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="landexpApp.housePhoto.name">Name</Translate>
              </span>
            </dt>
            <dd>{housePhoto.name}</dd>
            <dt>
              <span id="image">
                <Translate contentKey="landexpApp.housePhoto.image">Image</Translate>
              </span>
            </dt>
            <dd>
              {housePhoto.image ? (
                <div>
                  <a onClick={openFile(housePhoto.imageContentType, housePhoto.image)}>
                    <img src={`data:${housePhoto.imageContentType};base64,${housePhoto.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {housePhoto.imageContentType}, {byteSize(housePhoto.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.housePhoto.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={housePhoto.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.housePhoto.house">House</Translate>
            </dt>
            <dd>{housePhoto.houseId ? housePhoto.houseId : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.housePhoto.user">User</Translate>
            </dt>
            <dd>{housePhoto.userLogin ? housePhoto.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/house-photo" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/house-photo/${housePhoto.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ housePhoto }: IRootState) => ({
  housePhoto: housePhoto.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HousePhotoDetail);
