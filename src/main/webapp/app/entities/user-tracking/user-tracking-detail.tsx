import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-tracking.reducer';
import { IUserTracking } from 'app/shared/model/user-tracking.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserTrackingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class UserTrackingDetail extends React.Component<IUserTrackingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userTracking } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.userTracking.detail.title">UserTracking</Translate> [<b>{userTracking.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="activityType">
                <Translate contentKey="landexpApp.userTracking.activityType">Activity Type</Translate>
              </span>
            </dt>
            <dd>{userTracking.activityType}</dd>
            <dt>
              <span id="sourceId">
                <Translate contentKey="landexpApp.userTracking.sourceId">Source Id</Translate>
              </span>
            </dt>
            <dd>{userTracking.sourceId}</dd>
            <dt>
              <span id="sourceLink">
                <Translate contentKey="landexpApp.userTracking.sourceLink">Source Link</Translate>
              </span>
            </dt>
            <dd>{userTracking.sourceLink}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="landexpApp.userTracking.description">Description</Translate>
              </span>
            </dt>
            <dd>{userTracking.description}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.userTracking.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userTracking.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.userTracking.user">User</Translate>
            </dt>
            <dd>{userTracking.userLogin ? userTracking.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-tracking" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-tracking/${userTracking.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ userTracking }: IRootState) => ({
  userTracking: userTracking.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserTrackingDetail);
