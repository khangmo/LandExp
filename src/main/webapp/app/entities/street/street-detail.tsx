import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './street.reducer';
import { IStreet } from 'app/shared/model/street.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStreetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class StreetDetail extends React.Component<IStreetDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { street } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.street.detail.title">Street</Translate> [<b>{street.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="landexpApp.street.name">Name</Translate>
              </span>
            </dt>
            <dd>{street.name}</dd>
            <dt>
              <span id="postalCode">
                <Translate contentKey="landexpApp.street.postalCode">Postal Code</Translate>
              </span>
            </dt>
            <dd>{street.postalCode}</dd>
            <dt>
              <span id="stateProvince">
                <Translate contentKey="landexpApp.street.stateProvince">State Province</Translate>
              </span>
            </dt>
            <dd>{street.stateProvince}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="landexpApp.street.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{street.enabled ? 'true' : 'false'}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.street.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={street.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.street.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={street.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.street.district">District</Translate>
            </dt>
            <dd>{street.districtName ? street.districtName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/street" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/street/${street.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ street }: IRootState) => ({
  street: street.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StreetDetail);
