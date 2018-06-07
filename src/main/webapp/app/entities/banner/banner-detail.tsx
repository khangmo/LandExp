import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './banner.reducer';
import { IBanner } from 'app/shared/model/banner.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBannerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class BannerDetail extends React.Component<IBannerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { banner } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.banner.detail.title">Banner</Translate> [<b>{banner.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">
                <Translate contentKey="landexpApp.banner.title">Title</Translate>
              </span>
            </dt>
            <dd>{banner.title}</dd>
            <dt>
              <span id="titleAlias">
                <Translate contentKey="landexpApp.banner.titleAlias">Title Alias</Translate>
              </span>
            </dt>
            <dd>{banner.titleAlias}</dd>
            <dt>
              <span id="area">
                <Translate contentKey="landexpApp.banner.area">Area</Translate>
              </span>
            </dt>
            <dd>{banner.area}</dd>
            <dt>
              <span id="hits">
                <Translate contentKey="landexpApp.banner.hits">Hits</Translate>
              </span>
            </dt>
            <dd>{banner.hits}</dd>
            <dt>
              <span id="publicUp">
                <Translate contentKey="landexpApp.banner.publicUp">Public Up</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={banner.publicUp} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="publicDown">
                <Translate contentKey="landexpApp.banner.publicDown">Public Down</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={banner.publicDown} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.banner.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={banner.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.banner.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={banner.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.banner.createBy">Create By</Translate>
            </dt>
            <dd>{banner.createByLogin ? banner.createByLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.banner.updateBy">Update By</Translate>
            </dt>
            <dd>{banner.updateByLogin ? banner.updateByLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/banner" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/banner/${banner.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ banner }: IRootState) => ({
  banner: banner.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BannerDetail);
