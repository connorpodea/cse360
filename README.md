FoundationsF26
A role-based course discussion board with staff moderation and grading support, built as a JavaFX desktop application in Java.

Students post questions, staff and admins reply and leave private feedback, staff escalate issues to admins as trackable requests, and the instructional team can analyze discussion activity to verify participation — all through a single JavaFX GUI (MVC pattern throughout).

Features
- Role-Based Accounts — Admin, Student, and Reviewer/Staff roles per user; an account can hold multiple roles and is routed through a dispatch screen to pick one at login
- First-Admin Bootstrap — on first run (empty database) the app forces creation of the initial admin account instead of shipping hard-coded credentials
- Invitation-Code Signup — admins generate single-use, role-scoped invitation codes; new accounts are created by redeeming a code tied to an email address
- Discussion Board — students create categorized posts (e.g. Question); anyone can reply, and posts can be logically deleted without losing history
- Staff Whispers — staff can attach a private note to a student's post that only that student sees, separate from public replies
- Staff Requests — staff submit requests to the admin with a title/description; admin can close them with resolution notes, and closed requests can be reopened (linked back to the original)
- Discussion Analyzer — grading support that counts distinct students a given student has answered (not just reply count), to verify participation requirements
- Unanswered-Question Filter — surfaces open, non-deleted questions with no external replies
- Participation Summaries — per-student rollups (posts, replies, distinct students answered, requirement met) for staff review
- Add/Remove Roles — admins grant or revoke Admin/Student/Reviewer roles on existing accounts
- One-Time Password Reset — admins issue a temporary password; the affected user is forced to set a new one on next login
- Input Validators — finite-state-machine-based recognizers enforce username, password, and email format rules at entry time

Tech Stack
| Layer | Technology |
|---|---|
| Language | Java (JavaFX for the UI, standard library otherwise) |
| Database | H2 (file-backed, embedded) via JDBC |
| GUI Pattern | Model-View-Controller, one package per feature/screen |
| Build | Eclipse project (JavaFX SDK + H2 as user libraries) — no Maven/Gradle |
| Testing | JUnit 5 |

Project Structure
```
HW2/src/
├── applicationMain/
│   └── FoundationsMain.java       # Entry point — DB connect, first-run check, launches login/admin setup
├── database/
│   └── Database.java              # JDBC connection, schema creation, all queries
├── entityClasses/
│   ├── User.java, Post.java, Reply.java, Request.java, WhisperMessage.java
│   ├── PostStorage.java, ReplyStorage.java   # In-memory collections backing the board
│   ├── DiscussionAnalyzer.java     # Distinct-students-answered grading logic
│   ├── FilterUnanswered.java       # Unanswered-question filter
│   └── StudentParticipationSummary.java
├── loginRouting/
│   └── LoginService.java          # Pure role -> destination routing logic (unit-testable)
├── guiFirstAdmin/                 # First-run admin account setup
├── guiUserLogin/                  # Login screen
├── guiNewAccount/                 # Invitation-code account creation
├── guiOneTimePassword/            # Admin-issued temporary password flow
├── guiUserUpdate/                 # Profile self-service
├── guiAddRemoveRoles/             # Admin: grant/revoke roles
├── guiAdminHome/                  # Admin dashboard, invitation codes, user list
├── guiMultipleRoleDispatch/       # Role picker for multi-role accounts
├── guiRole1/ guiRole2/            # Student / Reviewer home screens
├── guiTools/                      # UserName/Password/Email FSM recognizers
├── MVCPostManagement/             # Post creation and moderation MVC
├── MVCReplyManagement/            # Reply creation and listing MVC
├── MVCRequestManagement/          # Staff request MVC
├── testCases/                     # JUnit tests
└── DataBaseTests/                 # Manual + automated DB/post/reply/staff test drivers
```

Getting Started
Requirements: Java 17+, JavaFX SDK, H2 database jar, Eclipse (project is configured as Eclipse user libraries, not Maven/Gradle)

```
# Clone the repo
git clone https://github.com/connorpodea/cse360
cd cse360/HW2

# Import into Eclipse as an existing project, then add two user libraries:
#   - javaFX  -> pointing at your local JavaFX SDK lib/
#   - H2      -> pointing at the H2 jar
# (see .classpath for the expected library names)

# Run applicationMain/FoundationsMain.java as a Java Application
```

The H2 database file is created automatically under your home directory on first run. On an empty database, the app forces you through first-admin setup before anything else is usable.

Database Schema
7 tables covering accounts, the discussion board, and staff workflows:

| Table | Purpose |
|---|---|
| userDB | Accounts — name, email, password, and Admin/Student/Reviewer role flags, OTP flag |
| postDB | Discussion posts — title, body, author, category, staff feedback, soft-delete flag |
| replyDB | Replies to posts |
| whisperDB | Private staff-to-student notes attached to a post |
| requestDB | Staff-to-admin requests — status (OPEN/CLOSED/REOPENED), resolution notes, link to original request |
| InvitationCodes | Single-use signup codes scoped to an email address and role |
| reviewParameters | Named grading/review criteria |

Security
- Role-based access control — GUI screens and admin actions are gated by the Admin/Student/Reviewer flags on the logged-in user
- Invitation-only signup — new accounts require a valid, single-use code generated by an admin for a specific email and role
- No hard-coded credentials — the very first admin account must be created interactively on first launch
- Forced password reset — temporary passwords issued via the One-Time Password flow must be changed by the user before continuing
- Input validation — dedicated FSM recognizers reject malformed usernames, passwords, and emails before they reach the database

Author
Connor Podea — CS student project, built to learn Java desktop application design (MVC, JavaFX), embedded relational databases, and role-based access control.
