package tasks

import contributors.*
import java.util.concurrent.atomic.AtomicInteger

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service
            .getOrgRepos(req.org)
            .also { logRepos(req, it) }
            .body() ?: listOf()
    val allUsers = mutableListOf<User>()
    val counter = AtomicInteger(0)
    for (repo in repos) {
        val users = service.getRepoContributors(req.org, repo.name)
                .also{ logUsers(repo, it)}
                .body() ?: listOf()
        allUsers += users
        updateResults(allUsers.aggregate(),counter.incrementAndGet() == repos.size)
    }
}
