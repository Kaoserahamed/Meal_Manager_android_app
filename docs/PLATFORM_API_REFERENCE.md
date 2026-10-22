# Platform Expansion API Reference

## Payments
### POST /api/v1/payments/intents
Create a payment intent. Body: `PaymentIntentDto { amount, currency, paymentMethodId?, description? }`

### GET /api/v1/payments/{id}
Get payment by ID.

### POST /api/v1/payments/{id}/refund
Refund a payment. Body: `RefundDto { amount, currency, reason? }`

### GET /api/v1/payments
List payments. Query: `page`, `size`

### POST /api/v1/payments/webhook/stripe
Stripe webhook endpoint.

### POST /api/v1/payments/webhook/paypal
PayPal webhook endpoint.

### GET /api/v1/payments/methods
List available payment methods.

### POST /api/v1/payments/methods
Add a payment method. Body: `PaymentMethodDto`

## Delivery
### POST /api/v1/delivery
Create a delivery order. Body: `DeliveryDto`

### GET /api/v1/delivery/{id}
Get delivery by ID.

### PUT /api/v1/delivery/{id}/status
Update delivery status. Body: `DeliveryStatusRequest { status }`

### POST /api/v1/delivery/{id}/assign
Assign a driver. Body: `DeliveryAssignmentDto { driverId }`

### GET /api/v1/delivery
List deliveries. Query: `status?`, `page`, `size`

### GET /api/v1/delivery/drivers
List drivers.

### GET /api/v1/delivery/{id}/tracking
Get real-time tracking coordinates.

## Calendar
### GET /api/v1/calendar
List events in date range. Query: `startDate`, `endDate`

### POST /api/v1/calendar
Create event. Body: `CalendarEventDto`

### GET /api/v1/calendar/{id}
Get event by ID.

### PUT /api/v1/calendar/{id}
Update event.

### DELETE /api/v1/calendar/{id}
Delete event.

### GET /api/v1/calendar/meal-plans
List meal plan calendar events.

### POST /api/v1/calendar/sync/google
Sync Google Calendar.

## Notifications
### POST /api/v1/notifications
Send notification. Body: `NotificationDto`

### POST /api/v1/notifications/broadcast
Broadcast to users. Body: `BroadcastNotificationRequest`

### GET /api/v1/notifications
List notifications.

### PUT /api/v1/notifications/{id}/read
Mark as read.

### PUT /api/v1/notifications/preferences
Update preferences. Body: `NotificationPreferenceDto`

### GET /api/v1/notifications/preferences
Get preferences.

### POST /api/v1/notifications/push/register
Register push token.

## Analytics
### GET /api/v1/analytics/summary
Get metrics summary. Query: `startDate`, `endDate`

### GET /api/v1/analytics/reports
List reports.

### POST /api/v1/analytics/reports
Generate report. Body: `GenerateReportRequest`

### GET /api/v1/analytics/reports/{id}
Get report by ID.

### GET /api/v1/analytics/trends/nutrition
Nutrition trends. Query: `range`

### GET /api/v1/analytics/trends/engagement
Engagement trends. Query: `range`

## ML / Recommendations
### GET /api/v1/ml/recommendations/recipes
Recipe recommendations.

### GET /api/v1/ml/recommendations/meals
Meal recommendations.

### POST /api/v1/ml/personalization
Update personalization. Body: `PersonalizationDto`

### GET /api/v1/ml/personalization
Get personalization.

### GET /api/v1/ml/collaborative-filtering
Collaborative filtering results.

### GET /api/v1/ml/content-based
Content-based results.

### POST /api/v1/ml/train
Train recommendation model.

## Organizations (Multi-tenant)
### POST /api/v1/organizations
Create organization. Body: `OrganizationDto`

### GET /api/v1/organizations/{id}
Get organization.

### PUT /api/v1/organizations/{id}
Update organization.

### DELETE /api/v1/organizations/{id}
Delete organization.

### GET /api/v1/organizations
List organizations.

### POST /api/v1/organizations/{orgId}/memberships
Add membership. Body: `MembershipDto`

### GET /api/v1/organizations/{orgId}/memberships
List memberships.

### DELETE /api/v1/organizations/{orgId}/memberships/{userId}
Remove membership.

## Webhooks
### POST /api/v1/webhooks/endpoints
Create webhook endpoint. Body: `WebhookEndpointDto`

### GET /api/v1/webhooks/endpoints
List endpoints.

### DELETE /api/v1/webhooks/endpoints/{id}
Delete endpoint.

### POST /api/v1/webhooks/endpoints/{id}/deliver
Deliver webhook.

### GET /api/v1/webhooks/deliveries
List deliveries.

### POST /api/v1/webhooks/ingest/{endpointId}
Ingest payload.

## Search
### GET /api/v1/search
Search all content. Query: `q`, `page`, `size`

### GET /api/v1/search/recipes
Search recipes.

### GET /api/v1/search/suggestions
Get autocomplete suggestions. Query: `q`

## Cache
### GET /api/v1/cache/stats
Get cache statistics.

### DELETE /api/v1/cache
Clear cache. Query: `key?`

### GET /api/v1/cache/{key}
Get cache entry.

## Rate Limit
### GET /api/v1/rate-limit/status
Get rate limit status.

### GET /api/v1/rate-limit/config
Get rate limit config.

## Admin
### GET /api/v1/admin/users
List users.

### GET /api/v1/admin/health
System health.

### GET /api/v1/admin/stats
System statistics.

## Moderation
### GET /api/v1/moderation/queue
Moderation queue.

### POST /api/v1/moderation/actions
Take moderation action. Body: `ModerationActionDto`

### GET /api/v1/moderation/reviews/{id}
Get review item.

## Loyalty
### GET /api/v1/loyalty/{userId}/account
Get loyalty account.

### POST /api/v1/loyalty/points
Add points. Body: `AddPointsRequest`

### GET /api/v1/loyalty/rewards
List rewards.

### POST /api/v1/loyalty/rewards/redeem
Redeem reward. Body: `RedeemRewardRequest`

## Subscriptions
### GET /api/v1/subscriptions/{userId}
Get subscription.

### POST /api/v1/subscriptions
Create subscription. Body: `SubscriptionDto`

### POST /api/v1/subscriptions/{id}/cancel
Cancel subscription.

### GET /api/v1/subscriptions/invoices
List invoices.

### POST /api/v1/subscriptions/webhook/stripe
Stripe subscription webhook.
