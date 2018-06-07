import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-subscription.reducer';
import { IUserSubscription } from 'app/shared/model/user-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserSubscriptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class UserSubscriptionDetail extends React.Component<IUserSubscriptionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userSubscription } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.userSubscription.detail.title">UserSubscription</Translate> [<b>{userSubscription.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="actionType">
                <Translate contentKey="landexpApp.userSubscription.actionType">Action Type</Translate>
              </span>
            </dt>
            <dd>{userSubscription.actionType}</dd>
            <dt>
              <span id="keyword">
                <Translate contentKey="landexpApp.userSubscription.keyword">Keyword</Translate>
              </span>
            </dt>
            <dd>{userSubscription.keyword}</dd>
            <dt>
              <span id="costFrom">
                <Translate contentKey="landexpApp.userSubscription.costFrom">Cost From</Translate>
              </span>
            </dt>
            <dd>{userSubscription.costFrom}</dd>
            <dt>
              <span id="costTo">
                <Translate contentKey="landexpApp.userSubscription.costTo">Cost To</Translate>
              </span>
            </dt>
            <dd>{userSubscription.costTo}</dd>
            <dt>
              <span id="acreageFrom">
                <Translate contentKey="landexpApp.userSubscription.acreageFrom">Acreage From</Translate>
              </span>
            </dt>
            <dd>{userSubscription.acreageFrom}</dd>
            <dt>
              <span id="acreageTo">
                <Translate contentKey="landexpApp.userSubscription.acreageTo">Acreage To</Translate>
              </span>
            </dt>
            <dd>{userSubscription.acreageTo}</dd>
            <dt>
              <span id="direction">
                <Translate contentKey="landexpApp.userSubscription.direction">Direction</Translate>
              </span>
            </dt>
            <dd>{userSubscription.direction}</dd>
            <dt>
              <span id="floor">
                <Translate contentKey="landexpApp.userSubscription.floor">Floor</Translate>
              </span>
            </dt>
            <dd>{userSubscription.floor}</dd>
            <dt>
              <span id="bathRoom">
                <Translate contentKey="landexpApp.userSubscription.bathRoom">Bath Room</Translate>
              </span>
            </dt>
            <dd>{userSubscription.bathRoom}</dd>
            <dt>
              <span id="bedRoom">
                <Translate contentKey="landexpApp.userSubscription.bedRoom">Bed Room</Translate>
              </span>
            </dt>
            <dd>{userSubscription.bedRoom}</dd>
            <dt>
              <span id="parking">
                <Translate contentKey="landexpApp.userSubscription.parking">Parking</Translate>
              </span>
            </dt>
            <dd>{userSubscription.parking ? 'true' : 'false'}</dd>
            <dt>
              <span id="landType">
                <Translate contentKey="landexpApp.userSubscription.landType">Land Type</Translate>
              </span>
            </dt>
            <dd>{userSubscription.landType}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="landexpApp.userSubscription.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{userSubscription.enabled ? 'true' : 'false'}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.userSubscription.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userSubscription.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.userSubscription.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userSubscription.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.userSubscription.user">User</Translate>
            </dt>
            <dd>{userSubscription.userLogin ? userSubscription.userLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.userSubscription.city">City</Translate>
            </dt>
            <dd>{userSubscription.cityName ? userSubscription.cityName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.userSubscription.street">Street</Translate>
            </dt>
            <dd>{userSubscription.streetName ? userSubscription.streetName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-subscription" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-subscription/${userSubscription.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ userSubscription }: IRootState) => ({
  userSubscription: userSubscription.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserSubscriptionDetail);
