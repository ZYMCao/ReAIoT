package cn.easttrans.reaiot.domain;

public final class ModelConstants {
    /**
     * Generic constants.
     */
    public static final String ID = "id";
    public static final String CREATED_TIME = "created_time";
    public static final String USER_ID = "user_id";
    public static final String TENANT_ID = "tenant_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String ASSIGNEE_ID = "assignee_id";
    public static final String DEVICE_ID = "device_id";
    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String ALIAS = "alias";
    public static final String CONFIGURATION = "configuration";
    public static final String SEARCH_TEXT = "search_text";
    public static final String ADDITIONAL_INFO = "additional_info";
    public static final String ENTITY_TYPE = "entity_type";
    public static final String ENTITY_ID = "entity_id";
    public static final String ENTITY_NAME = "entity_name";
    public static final String ATTRIBUTE_TYPE = "attribute_type";
    public static final String ATTRIBUTE_KEY = "attribute_key";
    public static final String LAST_UPDATE_TS = "last_update_ts";
    public static final String OWNER_NAME = "owner_name";
    public static final String OWNER_IDS = "owner_ids";
    public static final String GROUPS = "groups";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String IS_DEFAULT = "is_default";
    public static final String ROUTING_KEY = "routing_key";
    public static final String SECRET = "secret";
    public static final String DEBUG_MODE = "debug_mode";
    public static final String PROFILE_DATA = "profile_data";

    /**
     * Default constants.
     */
    public static final String DEFAULT_DASHBOARD_NAME = "basic_default_dashboard_name";
    public static final String DEFAULT_DASHBOARD_ID = "default_dashboard_id";
    public static final String DEFAULT_EDGE_RULE_CHAIN_ID = "default_edge_rule_chain_id";
    public static final String DEFAULT_QUEUE_NAME = "default_queue_name";
    public static final String DEFAULT_RULE_CHAIN_ID = "default_rule_chain_id";

    /**
     * User & Contact constants.
     */
    public static final String USER_TABLE_NAME = "tb_user";
    public static final String AUTHORITY = "authority";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String ADDRESS2 = "address2";
    public static final String ZIP = "zip";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    /**
     * User_credentials constants.
     */

    public static final String USER_INFO_VIEW_TABLE_NAME = "user_info_view";
    public static final String USER_CREDENTIALS_TABLE_NAME = "user_credentials";
    public static final String USER_CREDENTIALS_ENABLED = "enabled";
    public static final String USER_CREDENTIALS_PASSWORD = "password"; //NOSONAR, the constant used to identify password column name (not password value itself)
    public static final String USER_CREDENTIALS_ACTIVATE_TOKEN = "activate_token";
    public static final String USER_CREDENTIALS_RESET_TOKEN = "reset_token";
    /**
     * User settings constants.
     */
    public static final String USER_SETTINGS_TABLE_NAME = "user_settings";
    public static final String USER_SETTINGS_TYPE = "type";
    public static final String USER_SETTINGS_SETTINGS = "settings";
    public static final String WHITE_LABELING_TABLE_NAME = "white_labeling";
    public static final String WHITE_LABELING_SETTINGS_TYPE = "type";
    public static final String WHITE_LABELING_SETTINGS = "settings";
    public static final String WHITE_LABELING_DOMAIN = "domain_name";

    /**
     * Admin_settings constants.
     */
    public static final String ADMIN_SETTINGS_TABLE_NAME = "admin_settings";

    public static final String ADMIN_SETTINGS_KEY = "key";
    public static final String ADMIN_SETTINGS_JSON_VALUE = "json_value";

    /**
     * Tenant constants.
     */
    public static final String TENANT_TABLE_NAME = "tenant";
    public static final String REGION = "region";
    public static final String TENANT_PROFILE_ID = "tenant_profile_id";

    /**
     * Tenant profile constants.
     */
    public static final String TENANT_PROFILE_TABLE_NAME = "tenant_profile";
    public static final String ISOLATED_TB_RULE_ENGINE = "isolated_tb_rule_engine";

    /**
     * Customer constants.
     */
    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String PARENT_CUSTOMER_ID = "parent_customer_id";
    public static final String CUSTOMER_INFO_VIEW_TABLE_NAME = "customer_info_view";
    public static final String OWNER_INFO_VIEW_TABLE_NAME = "owner_info_view";
    public static final String OWNER_INFO_VIEW_IS_PUBLIC = "is_public";

    /**
     * Device constants.
     */
    public static final String DEVICE_TABLE_NAME = "device";
    public static final String DEVICE_DEVICE_PROFILE_ID = "device_profile_id";
    public static final String DEVICE_DEVICE_DATA = "device_data";
    public static final String FIRMWARE_ID = "firmware_id";
    public static final String SOFTWARE_ID = "software_id";
    public static final String DEVICE_ACTIVE = "active";

    public static final String DEVICE_INFO_VIEW_TABLE_NAME = "device_info_view";

    /**
     * Device profile constants.
     */
    public static final String DEVICE_PROFILE_TABLE_NAME = "device_profile";
    public static final String TRANSPORT_TYPE = "transport_type";
    public static final String PROVISION_TYPE = "provision_type";
    public static final String PROVISION_DEVICE_KEY = "provision_device_key";

    /**
     * Asset profile constants.
     */
    public static final String ASSET_PROFILE_TABLE_NAME = "asset_profile";

    /**
     * Entity view constants.
     */
    public static final String ENTITY_VIEW_TABLE_NAME = "entity_view";
    public static final String ENTITY_VIEW_KEYS = "keys";
    public static final String ENTITY_VIEW_START_TS = "start_ts";
    public static final String ENTITY_VIEW_END_TS = "end_ts";
    public static final String ENTITY_VIEW_INFO_VIEW_TABLE_NAME = "entity_view_info_view";

    /**
     * Audit log constants.
     */
    public static final String AUDIT_LOG_TABLE_NAME = "audit_log";
    public static final String USER_NAME = "user_name";
    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_DATA = "action_data";
    public static final String ACTION_STATUS = "action_status";
    public static final String ACTION_FAILURE_DETAILS = "action_failure_details";

    /**
     * Asset constants.
     */
    public static final String ASSET_TABLE_NAME = "asset";

    public static final String ASSET_PROFILE_ID = "asset_profile_id";
    public static final String ASSET_INFO_VIEW_TABLE_NAME = "asset_info_view";
    public static final String CONVERTER_TABLE_NAME = "converter";
    public static final String CONVERTER_IS_EDGE_TEMPLATE_MODE = "is_edge_template";
    public static final String INTEGRATION_TABLE_NAME = "integration";
    public static final String CONVERTER_ID = "converter_id";
    public static final String DOWNLINK_CONVERTER_ID = "downlink_converter_id";
    public static final String ENABLED = "enabled";
    public static final String IS_REMOTE = "is_remote";
    public static final String ALLOW_CREATE_DEVICES_OR_ASSETS = "allow_create_devices_or_assets";
    public static final String IS_EDGE_TEMPLATE = "is_edge_template";
    public static final String INTEGRATION_VIEW_NAME = "integration_info";

    /**
     * Alarm constants.
     */
    public static final String ENTITY_ALARM_TABLE_NAME = "entity_alarm";
    public static final String ALARM_TABLE_NAME = "alarm";
    public static final String ALARM_VIEW_NAME = "alarm_info";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String ORIGINATOR_ID = "originator_id";
    public static final String ORIGINATOR_NAME = "originator_name";
    public static final String ORIGINATOR_LABEL = "originator_label";
    public static final String ORIGINATOR_TYPE = "originator_type";
    public static final String SEVERITY = "severity";
    public static final String ASSIGNEE_FIRST_NAME = "assignee_first_name";
    public static final String ASSIGNEE_LAST_NAME = "assignee_last_name";
    public static final String ASSIGNEE_EMAIL = "assignee_email";
    public static final String START_TS = "start_ts";
    public static final String END_TS = "end_ts";
    public static final String ACKNOWLEDGED = "acknowledged";
    public static final String ACK_TS = "ack_ts";
    public static final String CLEARED = "cleared";
    public static final String CLEAR_TS = "clear_ts";
    public static final String ASSIGN_TS = "assign_ts";
    public static final String PROPAGATE = "propagate";
    public static final String PROPAGATE_TO_OWNER = "propagate_to_owner";
    public static final String PROPAGATE_TO_OWNER_HIERARCHY = "propagate_to_owner_hierarchy";
    public static final String PROPAGATE_TO_TENANT = "propagate_to_tenant";
    public static final String PROPAGATE_RELATION_TYPES = "propagate_relation_types";

    public static final String COMMENT_TABLE_NAME = "alarm_comment";
    public static final String COMMENT_ALARM_ID = "alarm_id";
    public static final String COMMENT_TYPE = "type";
    public static final String COMMENT_COMMENT = "comment";

    /**
     * Entity relation constants.
     */
    public static final String RELATION_TABLE_NAME = "relation";
    public static final String FROM_ID = "from_id";
    public static final String FROM_TYPE = "from_type";
    public static final String TO_ID = "to_id";
    public static final String TO_TYPE = "to_type";
    public static final String RELATION_TYPE = "relation_type";
    public static final String RELATION_TYPE_GROUP = "relation_type_group";
    public static final String ENTITY_GROUP_TABLE_NAME = "entity_group";
    public static final String ENTITY_GROUP_TYPE = "type";
    public static final String ENTITY_GROUP_NAME = "name";
    public static final String ENTITY_GROUP_OWNER_ID = "owner_id";
    public static final String ENTITY_GROUP_OWNER_TYPE = "owner_type";
    public static final String ENTITY_GROUP_CONFIGURATION = "configuration";
    public static final String ENTITY_GROUP_INFO_VIEW_TABLE_NAME = "entity_group_info_view";

    /**
     * Device_credentials constants.
     */
    public static final String DEVICE_CREDENTIALS_TABLE_NAME = "device_credentials";
    public static final String DEVICE_CREDENTIALS_CREDENTIALS_TYPE = "credentials_type";
    public static final String DEVICE_CREDENTIALS_CREDENTIALS_ID = "credentials_id";
    public static final String DEVICE_CREDENTIALS_CREDENTIALS_VALUE = "credentials_value";

    /**
     * Widgets_bundle constants.
     */
    public static final String WIDGETS_BUNDLE_TABLE_NAME = "widgets_bundle";
    public static final String WIDGETS_BUNDLE_ORDER = "widgets_bundle_order";

    /**
     * Widget_type constants.
     */
    public static final String WIDGET_TYPE_TABLE_NAME = "widget_type";

    public static final String FQN = "fqn";
    public static final String TAGS = "tags";
    public static final String DESCRIPTOR = "descriptor";
    public static final String DEPRECATED = "deprecated";
    public static final String WIDGET_TYPE = "widget_type";
    public static final String WIDGET_TYPE_INFO_VIEW_TABLE_NAME = "widget_type_info_view";

    /**
     * Widgets bundle widget constants.
     */
    public static final String WIDGETS_BUNDLE_WIDGET_TABLE_NAME = "widgets_bundle_widget";

    public static final String WIDGET_TYPE_ORDER = "widget_type_order";

    /**
     * Dashboard constants.
     */
    public static final String DASHBOARD_TABLE_NAME = "dashboard";
    public static final String DASHBOARD_CONFIGURATION = "configuration";
    public static final String ASSIGNED_CUSTOMERS = "assigned_customers";
    public static final String MOBILE_HIDE = "mobile_hide";
    public static final String MOBILE_ORDER = "mobile_order";
    public static final String DASHBOARD_INFO_VIEW_TABLE_NAME = "dashboard_info_view";

    /**
     * Plugin component metadata constants.
     */
    public static final String COMPONENT_DESCRIPTOR_TABLE_NAME = "component_descriptor";
    public static final String COMPONENT_DESCRIPTOR_TYPE = "type";
    public static final String COMPONENT_DESCRIPTOR_SCOPE = "scope";
    public static final String COMPONENT_DESCRIPTOR_CLUSTERING_MODE = "clustering_mode";
    public static final String COMPONENT_DESCRIPTOR_NAME = "name";
    public static final String COMPONENT_DESCRIPTOR_CLASS = "clazz";
    public static final String COMPONENT_DESCRIPTOR_CONFIGURATION_DESCRIPTOR = "configuration_descriptor";
    public static final String COMPONENT_DESCRIPTOR_CONFIGURATION_VERSION = "configuration_version";
    public static final String COMPONENT_DESCRIPTOR_ACTIONS = "actions";
    public static final String COMPONENT_DESCRIPTOR_HAS_QUEUE_NAME = "has_queue_name";

    /**
     * Event constants.
     */
    public static final String ERROR_EVENT_TABLE_NAME = "error_event";
    public static final String LC_EVENT_TABLE_NAME = "lc_event";
    public static final String STATS_EVENT_TABLE_NAME = "stats_event";
    public static final String RAW_DATA_EVENT_TABLE_NAME = "raw_data_event";
    public static final String RULE_NODE_DEBUG_EVENT_TABLE_NAME = "rule_node_debug_event";
    public static final String RULE_CHAIN_DEBUG_EVENT_TABLE_NAME = "rule_chain_debug_event";
    public static final String CONVERTER_DEBUG_EVENT_TABLE_NAME = "converter_debug_event";
    public static final String INTEGRATION_DEBUG_EVENT_TABLE_NAME = "integration_debug_event";

    public static final String SERVICE_ID = "service_id";
    public static final String EVENT_ENTITY_ID = "entity_id";

    public static final String E_MESSAGES_PROCESSED = "e_messages_processed";
    public static final String E_ERRORS_OCCURRED = "e_errors_occurred";

    public static final String EVENT_METHOD_NAME = "e_method";

    public static final String E_TYPE = "e_type";
    public static final String E_ERROR = "e_error";
    public static final String E_SUCCESS = "e_success";

    public static final String EVENT_ENTITY_ID_NAME = "e_entity_id";
    public static final String EVENT_ENTITY_TYPE_NAME = "e_entity_type";
    public static final String EVENT_MSG_ID_NAME = "e_msg_id";
    public static final String EVENT_MSG_TYPE_NAME = "e_msg_type";
    public static final String EVENT_DATA_TYPE_NAME = "e_data_type";
    public static final String EVENT_RELATION_TYPE_NAME = "e_relation_type";
    public static final String EVENT_DATA_NAME = "e_data";
    public static final String EVENT_METADATA_NAME = "e_metadata";
    public static final String EVENT_MESSAGE_NAME = "e_message";
    public static final String EVENT_MESSAGE_TYPE_NAME = "e_message_type";
    public static final String EVENT_STATUS_NAME = "e_status";
    public static final String EVENT_UUID_NAME = "e_uuid";
    public static final String EVENT_IN_MSG_TYPE_NAME = "e_in_message_type";
    public static final String EVENT_IN_MSG_NAME = "e_in_message";
    public static final String EVENT_OUT_MSG_TYPE_NAME = "e_out_message_type";
    public static final String EVENT_OUT_MSG_NAME = "e_out_message";

    public static final String SINGLETON_MODE = "singleton_mode";
    public static final String EVENT_QUEUE_NAME = "queue_name";

    /**
     * Rule chain constants.
     */
    public static final String RULE_CHAIN_TABLE_NAME = "rule_chain";
    public static final String RULE_CHAIN_NAME = "name";
    public static final String RULE_CHAIN_TYPE = "type";
    public static final String RULE_CHAIN_FIRST_RULE_NODE_ID = "first_rule_node_id";
    public static final String RULE_CHAIN_ROOT = "root";
    public static final String RULE_CHAIN_CONFIGURATION = "configuration";

    /**
     * Rule node constants.
     */
    public static final String RULE_NODE_TABLE_NAME = "rule_node";
    public static final String RULE_NODE_CHAIN_ID = "rule_chain_id";
    public static final String RULE_NODE_TYPE = "type";
    public static final String RULE_NODE_NAME = "name";
    public static final String RULE_NODE_VERSION = "configuration_version";
    public static final String RULE_NODE_CONFIGURATION = "configuration";

    /**
     * Node state constants.
     */
    public static final String RULE_NODE_STATE_TABLE_NAME = "rule_node_state";
    public static final String RULE_NODE_STATE_NODE_ID = "rule_node_id";
    public static final String RULE_NODE_STATE_ENTITY_TYPE = "entity_type";
    public static final String RULE_NODE_STATE_ENTITY_ID = "entity_id";
    public static final String RULE_NODE_STATE_DATA = "state_data";
    public static final String SCHEDULER_EVENT_TABLE_NAME = "scheduler_event";
    public static final String SCHEDULER_EVENT_ORIGINATOR_ID = "originator_id";
    public static final String SCHEDULER_EVENT_ORIGINATOR_TYPE = "originator_type";
    public static final String SCHEDULER_EVENT_NAME = "name";
    public static final String SCHEDULER_EVENT_TYPE = "type";
    public static final String SCHEDULER_EVENT_CONFIGURATION = "configuration";
    public static final String SCHEDULER_EVENT_SCHEDULE = "schedule";
    public static final String SCHEDULER_EVENT_ENABLED = "enabled";
    public static final String BLOB_ENTITY_TABLE_NAME = "blob_entity";
    public static final String BLOB_ENTITY_NAME = "name";
    public static final String BLOB_ENTITY_TYPE = "type";
    public static final String BLOB_ENTITY_CONTENT_TYPE = "content_type";
    public static final String BLOB_ENTITY_DATA = "data";
    public static final String ROLE_TABLE_NAME = "role";
    public static final String ROLE_NAME = "name";
    public static final String ROLE_TYPE = "type";
    public static final String ROLE_PERMISSIONS = "permissions";

    /**
     * OAuth2 client registration constants.
     */
    public static final String GROUP_PERMISSION_TABLE_NAME = "group_permission";
    public static final String GROUP_PERMISSION_ROLE_ID = "role_id";
    public static final String GROUP_PERMISSION_USER_GROUP_ID = "user_group_id";
    public static final String GROUP_PERMISSION_ENTITY_GROUP_ID = "entity_group_id";
    public static final String GROUP_PERMISSION_ENTITY_GROUP_TYPE = "entity_group_type";
    public static final String GROUP_PERMISSION_IS_PUBLIC = "is_public";
    public static final String OAUTH2_PARAMS_TABLE_NAME = "oauth2_params";
    public static final String OAUTH2_PARAMS_ENABLED = "enabled";

    public static final String OAUTH2_REGISTRATION_TABLE_NAME = "oauth2_registration";
    public static final String OAUTH2_DOMAIN_TABLE_NAME = "oauth2_domain";
    public static final String OAUTH2_MOBILE_TABLE_NAME = "oauth2_mobile";
    public static final String OAUTH2_PARAMS_ID = "oauth2_params_id";
    public static final String OAUTH2_PKG_NAME = "pkg_name";
    public static final String OAUTH2_APP_SECRET = "app_secret";

    public static final String OAUTH2_CLIENT_REGISTRATION_TEMPLATE_TABLE_NAME = "oauth2_client_registration_template";
    public static final String OAUTH2_TEMPLATE_PROVIDER_ID = "provider_id";
    public static final String OAUTH2_DOMAIN_NAME = "domain_name";
    public static final String OAUTH2_DOMAIN_SCHEME = "domain_scheme";
    public static final String OAUTH2_CLIENT_ID = "client_id";
    public static final String OAUTH2_CLIENT_SECRET = "client_secret";
    public static final String OAUTH2_AUTHORIZATION_URI = "authorization_uri";
    public static final String OAUTH2_TOKEN_URI = "token_uri";
    public static final String OAUTH2_SCOPE = "scope";
    public static final String OAUTH2_PLATFORMS = "platforms";
    public static final String OAUTH2_USER_INFO_URI = "user_info_uri";
    public static final String OAUTH2_USER_NAME_ATTRIBUTE_NAME = "user_name_attribute_name";
    public static final String OAUTH2_JWK_SET_URI = "jwk_set_uri";
    public static final String OAUTH2_CLIENT_AUTHENTICATION_METHOD = "client_authentication_method";
    public static final String OAUTH2_LOGIN_BUTTON_LABEL = "login_button_label";
    public static final String OAUTH2_LOGIN_BUTTON_ICON = "login_button_icon";
    public static final String OAUTH2_ALLOW_USER_CREATION = "allow_user_creation";
    public static final String OAUTH2_ACTIVATE_USER = "activate_user";
    public static final String OAUTH2_MAPPER_TYPE = "type";
    public static final String OAUTH2_EMAIL_ATTRIBUTE_KEY = "basic_email_attribute_key";
    public static final String OAUTH2_FIRST_NAME_ATTRIBUTE_KEY = "basic_first_name_attribute_key";
    public static final String OAUTH2_LAST_NAME_ATTRIBUTE_KEY = "basic_last_name_attribute_key";
    public static final String OAUTH2_TENANT_NAME_STRATEGY = "basic_tenant_name_strategy";
    public static final String OAUTH2_TENANT_NAME_PATTERN = "basic_tenant_name_pattern";
    public static final String OAUTH2_CUSTOMER_NAME_PATTERN = "basic_customer_name_pattern";
    public static final String OAUTH2_ALWAYS_FULL_SCREEN = "basic_always_full_screen";
    public static final String OAUTH2_PARENT_CUSTOMER_NAME_PATTERN = "basic_parent_customer_name_pattern";
    public static final String OAUTH2_USER_GROUPS_NAME_PATTERN = "basic_user_groups_name_pattern";
    public static final String OAUTH2_MAPPER_URL = "custom_url";
    public static final String OAUTH2_MAPPER_USERNAME = "custom_username";
    public static final String OAUTH2_MAPPER_PASSWORD = "custom_password";
    public static final String OAUTH2_MAPPER_SEND_TOKEN = "custom_send_token";
    public static final String OAUTH2_TEMPLATE_COMMENT = "comment";
    public static final String OAUTH2_TEMPLATE_HELP_LINK = "help_link";

    /**
     * Usage Record constants.
     */
    public static final String API_USAGE_STATE_TABLE_NAME = "api_usage_state";
    public static final String API_USAGE_STATE_TRANSPORT = "transport";
    public static final String API_USAGE_STATE_DB_STORAGE = "db_storage";
    public static final String API_USAGE_STATE_RE_EXEC = "re_exec";
    public static final String API_USAGE_STATE_JS_EXEC = "js_exec";
    public static final String API_USAGE_STATE_TBEL_EXEC = "tbel_exec";
    public static final String API_USAGE_STATE_EMAIL_EXEC = "email_exec";
    public static final String API_USAGE_STATE_SMS_EXEC = "sms_exec";
    public static final String API_USAGE_STATE_ALARM_EXEC = "alarm_exec";

    /**
     * Resource constants.
     */
    public static final String RESOURCE_TABLE_NAME = "resource";
    public static final String RESOURCE_TYPE = "resource_type";
    public static final String RESOURCE_KEY = "resource_key";
    public static final String RESOURCE_FILE_NAME = "file_name";
    public static final String RESOURCE_DATA = "data";
    public static final String RESOURCE_ETAG = "etag";
    public static final String RESOURCE_DESCRIPTOR = "descriptor";
    public static final String RESOURCE_PREVIEW = "preview";
    public static final String RESOURCE_IS_PUBLIC = "is_public";
    public static final String PUBLIC_RESOURCE_KEY = "public_resource_key";

    /**
     * Ota Package constants.
     */
    public static final String OTA_PACKAGE_TABLE_NAME = "ota_package";
    public static final String OTA_PACKAGE_TYPE = "type";
    public static final String OTA_PACKAGE_VERSION = "version";
    public static final String OTA_PACKAGE_TAG = "tag";
    public static final String OTA_PACKAGE_URL = "url";
    public static final String OTA_PACKAGE_FILE_NAME = "file_name";
    public static final String OTA_PACKAGE_CONTENT_TYPE = "content_type";
    public static final String OTA_PACKAGE_CHECKSUM_ALGORITHM = "checksum_algorithm";
    public static final String OTA_PACKAGE_CHECKSUM = "checksum";
    public static final String OTA_PACKAGE_DATA = "data";
    public static final String OTA_PACKAGE_DATA_SIZE = "data_size";

    public static final String DEVICE_GROUP_OTA_PACKAGE_TABLE_NAME = "device_group_ota_package";
    public static final String DEVICE_GROUP_OTA_PACKAGE_ID = "id";
    public static final String DEVICE_GROUP_OTA_PACKAGE_GROUP_ID = "group_id";
    public static final String DEVICE_GROUP_OTA_PACKAGE_FIRMWARE_TYPE = "ota_package_type";
    public static final String DEVICE_GROUP_OTA_PACKAGE_FIRMWARE_ID = "ota_package_id";
    public static final String DEVICE_GROUP_OTA_PACKAGE_FIRMWARE_UPDATE_TIME = "ota_package_update_time";
    /**
     * Persisted RPC constants.
     */
    public static final String RPC_TABLE_NAME = "rpc";
    public static final String RPC_DEVICE_ID = "device_id";
    public static final String RPC_EXPIRATION_TIME = "expiration_time";
    public static final String RPC_REQUEST = "request";
    public static final String RPC_RESPONSE = "response";
    public static final String RPC_STATUS = "status";
    /**
     * Edge constants.
     */
    public static final String EDGE_TABLE_NAME = "edge";
    public static final String ROOT_RULE_CHAIN_ID = "root_rule_chain_id";
    public static final String EDGE_LICENSE_KEY = "edge_license_key";
    public static final String CLOUD_ENDPOINT = "cloud_endpoint";
    public static final String EDGE_INFO_VIEW_TABLE_NAME = "edge_info_view";

    /**
     * Edge queue constants.
     */
    public static final String EDGE_EVENT_TABLE_NAME = "edge_event";
    public static final String EDGE_EVENT_SEQUENTIAL_ID = "seq_id";
    public static final String EDGE_EVENT_EDGE_ID = "edge_id";
    public static final String EDGE_EVENT_TYPE = "edge_event_type";
    public static final String EDGE_EVENT_ACTION = "edge_event_action";
    public static final String EDGE_EVENT_UID = "edge_event_uid";
    public static final String EDGE_EVENT_ENTITY_ID = "entity_id";
    public static final String EDGE_EVENT_BODY = "body";

    public static final String EXTERNAL_ID = "external_id";

    /**
     * User auth settings constants.
     */
    public static final String USER_AUTH_SETTINGS_TABLE_NAME = "user_auth_settings";
    public static final String USER_AUTH_SETTINGS_TWO_FA_SETTINGS = "two_fa_settings";

    /**
     * Cassandra attributes and timeseries constants.
     */
    public static final String TS_KV_CF = "ts_kv_cf";
    public static final String TS_KV_PARTITIONS_CF = "ts_kv_partitions_cf";
    public static final String TS_KV_LATEST_CF = "ts_kv_latest_cf";

    public static final String PARTITION = "partition";
    public static final String KEY = "key";
    public static final String KEY_ID = "key_id";
    public static final String TS = "ts";

    /**
     * Main names of cassandra key-value columns storage.
     */
    public static final String BOOLEAN_VALUE = "bool_v";
    public static final String STRING_VALUE = "str_v";
    public static final String LONG_VALUE = "long_v";
    public static final String DOUBLE_VALUE = "dbl_v";
    public static final String JSON_VALUE = "json_v";

    /**
     * Queue constants.
     */
    public static final String QUEUE_NAME = "name";
    public static final String QUEUE_TOPIC = "topic";
    public static final String QUEUE_POLL_INTERVAL = "poll_interval";
    public static final String QUEUE_PARTITIONS = "partitions";
    public static final String QUEUE_CONSUMER_PER_PARTITION = "consumer_per_partition";
    public static final String QUEUE_PACK_PROCESSING_TIMEOUT = "pack_processing_timeout";
    public static final String QUEUE_SUBMIT_STRATEGY = "submit_strategy";
    public static final String QUEUE_PROCESSING_STRATEGY = "processing_strategy";
    public static final String QUEUE_TABLE_NAME = "queue";

    /**
     * Notification constants
     */
    public static final String NOTIFICATION_TARGET_TABLE_NAME = "notification_target";
    public static final String NOTIFICATION_TARGET_CONFIGURATION = "configuration";

    public static final String NOTIFICATION_TABLE_NAME = "notification";
    public static final String REQUEST_ID = "request_id";
    public static final String RECIPIENT_ID = "recipient_id";
    public static final String DELIVERY_METHOD = "delivery_method";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String ADDITIONAL_CONFIG = "additional_config";

    public static final String NOTIFICATION_REQUEST_TABLE_NAME = "notification_request";
    public static final String NOTIFICATION_REQUEST_TARGETS = "targets";
    public static final String NOTIFICATION_REQUEST_TEMPLATE_ID = "template_id";
    public static final String NOTIFICATION_REQUEST_TEMPLATE = "template";
    public static final String NOTIFICATION_REQUEST_INFO = "info";
    public static final String NOTIFICATION_REQUEST_ORIGINATOR_ENTITY_ID = "originator_entity_id";
    public static final String NOTIFICATION_REQUEST_ORIGINATOR_ENTITY_TYPE = "originator_entity_type";
    public static final String NOTIFICATION_REQUEST_ADDITIONAL_CONFIG = "additional_config";
    public static final String NOTIFICATION_REQUEST_STATUS = "status";
    public static final String NOTIFICATION_REQUEST_RULE_ID = "rule_id";
    public static final String NOTIFICATION_REQUEST_STATS = "stats";

    public static final String NOTIFICATION_RULE_TABLE_NAME = "notification_rule";
    public static final String NOTIFICATION_RULE_ENABLED = "enabled";
    public static final String NOTIFICATION_RULE_TEMPLATE_ID = "template_id";
    public static final String NOTIFICATION_RULE_TRIGGER_TYPE = "trigger_type";
    public static final String NOTIFICATION_RULE_TRIGGER_CONFIG = "trigger_config";
    public static final String NOTIFICATION_RULE_RECIPIENTS_CONFIG = "recipients_config";
    public static final String NOTIFICATION_RULE_ADDITIONAL_CONFIG = "additional_config";

    public static final String NOTIFICATION_TEMPLATE_TABLE_NAME = "notification_template";
    public static final String NOTIFICATION_TEMPLATE_NOTIFICATION_TYPE = "notification_type";
    public static final String NOTIFICATION_TEMPLATE_CONFIGURATION = "configuration";

    public static final String SUB_CUSTOMERS_QUERY = " e.tenant_id = :tenantId AND e.customer_id IN (WITH RECURSIVE customers_ids(id) AS (SELECT id id FROM customer ce WHERE ce.tenant_id = :tenantId and id = :customerId UNION SELECT ce1.id id FROM customer ce1, customers_ids parent WHERE ce1.tenant_id = :tenantId and ce1.parent_customer_id = parent.id) SELECT id FROM customers_ids) ";
    public static final String CUSTOMERS_SUB_CUSTOMERS_QUERY = " e.tenant_id = :tenantId AND e.parent_customer_id IN (WITH RECURSIVE customers_ids(id) AS (SELECT id id FROM customer ce WHERE ce.tenant_id = :tenantId and id = :customerId UNION SELECT ce1.id id FROM customer ce1, customers_ids parent WHERE ce1.tenant_id = :tenantId and ce1.parent_customer_id = parent.id) SELECT id FROM customers_ids) ";

    protected static final String[] NONE_AGGREGATIONS = new String[]{LONG_VALUE, DOUBLE_VALUE, BOOLEAN_VALUE, STRING_VALUE, JSON_VALUE, KEY, TS};
}
