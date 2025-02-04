Events Platform EVENTIUM

A small community business can create and share events with members of the community.
A mobile app that allows community members to view, sign up for, and add events to their own personal calendars.
Staff members have additional functionality to create and manage events.



Minimum Viable Product (MVP)

The platform fulfils the following functionality:

1. Display a list of events for users to browse.

2. Allow users to sign up for an event.

3. Allow users to add events to their Google Calendar after signing up.

4. Enable staff members to sign-in to create and manage events.



Tech Choices:

• The platform should be built using Java or Kotlin.

• Event Data: You can use either a freely available API for event data or create your own event data.

  Research and decide on which API to use prior to starting. The focus is on building the platform, not on data generation.
  
• Calendar API: You'll need to sign up for the Google Calendar API (or an equivalent) using a free developer account. This will allow users to add events to their calendars.

• Implement security best practices for user authentication using OAuth login flow/username and password (e.g., Google Sign-In, GitHub OAuth)



The following technologies and tools are suggestions, not requirements:

• Android for the frontend.
• Kotlin for a new challenge.
• Google Calendar API for calendar integration.
• Google Sign-In for OAuth social sign in



UI Requirements
• Design should be responsive and adapt well across various mobile device screen sizes.

• Ensure accessibility for users with disabilities (e.g., support screen readers, voice navigation).

• The UI should clearly display errors (e.g., failed requests or missing fields) and show loading states when content is being fetched.

• The user interface should be intuitive, making it easy to find, sign up for, and create events.



Optional Extensions

If you have time once you have completed the MVP requirements, consider adding the following features:

1. Payment platform integration: Implement payments via Stripe, Google Pay, etc.

2. Confirmation emails: Automatically send confirmation emails to users who sign up for an event.

3. Social media integration: Allow users to share events on social platforms.

4. Cross-platform: Build both a mobile app and website.

5. Spring Security implement full authentication using the Spring Security framework for secure communication between the backend API and frontend Android app.


================================================================================


Database


DB Diagram version 1: https://dbdiagram.io/d/eventium-db-v-1-67a1434a263d6cf9a0e82cd3


![image](https://github.com/user-attachments/assets/80deeba0-f196-42a5-85e1-00605699727e)




