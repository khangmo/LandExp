import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './house-tracking.reducer';
import { IHouseTracking } from 'app/shared/model/house-tracking.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHouseTrackingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class HouseTrackingDetail extends React.Component<IHouseTrackingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { houseTracking } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.houseTracking.detail.title">HouseTracking</Translate> [<b>{houseTracking.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="activityType">
                <Translate contentKey="landexpApp.houseTracking.activityType">Activity Type</Translate>
              </span>
            </dt>
            <dd>{houseTracking.activityType}</dd>
            <dt>
              <span id="sourceId">
                <Translate contentKey="landexpApp.houseTracking.sourceId">Source Id</Translate>
              </span>
            </dt>
            <dd>{houseTracking.sourceId}</dd>
            <dt>
              <span id="sourceLink">
                <Translate contentKey="landexpApp.houseTracking.sourceLink">Source Link</Translate>
              </span>
            </dt>
            <dd>{houseTracking.sourceLink}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="landexpApp.houseTracking.description">Description</Translate>
              </span>
            </dt>
            <dd>{houseTracking.description}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.houseTracking.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={houseTracking.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.houseTracking.house">House</Translate>
            </dt>
            <dd>{houseTracking.houseId ? houseTracking.houseId : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.houseTracking.user">User</Translate>
            </dt>
            <dd>{houseTracking.userLogin ? houseTracking.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/house-tracking" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/house-tracking/${houseTracking.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ houseTracking }: IRootState) => ({
  houseTracking: houseTracking.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HouseTrackingDetail);
