import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './potential-customer.reducer';
import { IPotentialCustomer } from 'app/shared/model/potential-customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPotentialCustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class PotentialCustomerDetail extends React.Component<IPotentialCustomerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { potentialCustomer } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.potentialCustomer.detail.title">PotentialCustomer</Translate> [<b>{potentialCustomer.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="level">
                <Translate contentKey="landexpApp.potentialCustomer.level">Level</Translate>
              </span>
            </dt>
            <dd>{potentialCustomer.level}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="landexpApp.potentialCustomer.description">Description</Translate>
              </span>
            </dt>
            <dd>{potentialCustomer.description}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.potentialCustomer.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={potentialCustomer.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.potentialCustomer.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={potentialCustomer.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.potentialCustomer.customer">Customer</Translate>
            </dt>
            <dd>{potentialCustomer.customerLogin ? potentialCustomer.customerLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.potentialCustomer.createBy">Create By</Translate>
            </dt>
            <dd>{potentialCustomer.createByLogin ? potentialCustomer.createByLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.potentialCustomer.updateBy">Update By</Translate>
            </dt>
            <dd>{potentialCustomer.updateByLogin ? potentialCustomer.updateByLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/potential-customer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/potential-customer/${potentialCustomer.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ potentialCustomer }: IRootState) => ({
  potentialCustomer: potentialCustomer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotentialCustomerDetail);
