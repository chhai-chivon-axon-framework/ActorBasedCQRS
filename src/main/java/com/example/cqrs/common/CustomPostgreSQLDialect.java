package com.example.cqrs.common;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class CustomPostgreSQLDialect extends PostgreSQL94Dialect {

	public CustomPostgreSQLDialect() {
		super();
		this.registerColumnType(Types.BLOB, "BYTEA");
	}

	@Override
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
		if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
			return BinaryTypeDescriptor.INSTANCE;
		}
		return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
	}

}