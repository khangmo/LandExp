import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service-fee.reducer';
import { IServiceFee } from 'app/shared/model/service-fee.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceFeeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ServiceFeeDetail extends React.Component<IServiceFeeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceFee } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.serviceFee.detail.title">ServiceFee</Translate> [<b>{serviceFee.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="saleType">
                <Translate contentKey="landexpApp.serviceFee.saleType">Sale Type</Translate>
              </span>
            </dt>
            <dd>{serviceFee.saleType}</dd>
            <dt>
              <span id="fee">
                <Translate contentKey="landexpApp.serviceFee.fee">Fee</Translate>
              </span>
            </dt>
            <dd>{serviceFee.fee}</dd>
          </dl>
          <Button tag={Link} to="/entity/service-fee" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/service-fee/${serviceFee.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ serviceFee }: IRootState) => ({
  serviceFee: serviceFee.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ServiceFeeDetail);
