import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-region.reducer';
import { IUserRegion } from 'app/shared/model/user-region.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserRegionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class UserRegionDetail extends React.Component<IUserRegionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userRegion } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.userRegion.detail.title">UserRegion</Translate> [<b>{userRegion.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.userRegion.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userRegion.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.userRegion.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={userRegion.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.userRegion.user">User</Translate>
            </dt>
            <dd>{userRegion.userLogin ? userRegion.userLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.userRegion.region">Region</Translate>
            </dt>
            <dd>
              {userRegion.regions
                ? userRegion.regions.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.name}</a>
                      {i === userRegion.regions.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/user-region" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-region/${userRegion.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ userRegion }: IRootState) => ({
  userRegion: userRegion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserRegionDetail);
