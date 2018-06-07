import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-feed.reducer';
import { IUserFeed } from 'app/shared/model/user-feed.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserFeedDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class UserFeedDetail extends React.Component<IUserFeedDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userFeed } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.userFeed.detail.title">UserFeed</Translate> [<b>{userFeed.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="body">
                <Translate contentKey="landexpApp.userFeed.body">Body</Translate>
              </span>
            </dt>
            <dd>{userFeed.body}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.userFeed.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userFeed.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.userFeed.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userFeed.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.userFeed.user">User</Translate>
            </dt>
            <dd>{userFeed.userLogin ? userFeed.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-feed" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-feed/${userFeed.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ userFeed }: IRootState) => ({
  userFeed: userFeed.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserFeedDetail);
