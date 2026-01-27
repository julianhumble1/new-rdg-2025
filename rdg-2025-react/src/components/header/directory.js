export const navLinks = [
  { title: "Home", link: "/home" },
  {
    title: "About Us",
    link: "/about",
    sublinks: [
      {
        title: "Who We Are",
        link: "/about",
      },
      {
        title: "Our Venues",
        link: "/about/ourvenues",
      },
      {
        title: "Committee",
        link: "/about/committee",
      },
      {
        title: "Legal & Governance",
        link: "/about/legal",
      },
    ],
  },
  // { title: "What's On", link: "/upcoming" },
  {
    title: "Join Us",
    link: "/join",
    sublinks: [
      {
        title: "Get Involved",
        link: "/join",
      },
      {
        title: "Membership",
        link: "/join/membership",
      },
      {
        title: "Rehearsals & Auditions",
        link: "/join/rehearsals",
      },
    ],
  },
  { title: "Our History", link: "/archive" },
  { title: "Contact", link: "/contact" },
];
